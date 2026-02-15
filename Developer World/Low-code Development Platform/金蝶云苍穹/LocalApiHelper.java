package kd.iir.iiirs.business.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import kd.bos.cache.CacheFactory;
import kd.bos.cache.DistributeSessionlessCache;
import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.exception.ErrorCode;
import kd.bos.exception.KDException;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.openapi.base.script.OpenApiScript;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.util.HttpClientUtils;
import kd.bos.util.StringUtils;
import kd.iir.iiirs.common.constant.*;
import kd.iir.iiirs.common.utils.api.IiirsCustomApiUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author sun_jincheng
 * @version 1.0
 * @project 工业互联网平台标识解析
 * @description 本地 API 接口 Helper 类
 * @date 20:42:11 2023/10/17
 */
public final class LocalApiHelper {

    /**
     * 日志追踪器
     */
    private static final Log logger = LogFactory.getLog(LocalApiHelper.class);
    /**
     * 请求头页签
     */
    private static final String HEADER_ENTRY_ENTITY = "headerentryentity";
    /**
     * 请求体页签
     */
    private static final String BODY_ENTRY_ENTITY = "bodyentryentity";
    /**
     * Query 参数单据体
     */
    private static final String QUERY_ENTRY_ENTITY = "queryparaentryentity";
    /**
     * 响应体页签
     */
    private static final String RESP_ENTRY_ENTITY = "respentryentity";

    private static final String SERVER_IP = "serverip";
    private static final String REQ_PROTOCOL = "reqprotocol";
    private static final String PORT = "port";
    private static final String SEC_AUTH = "secauth";
    private static final String TOKEN_KEY = "tokenKey";
    private static final String DISTRIBUTED_UNIQUE_ID = "distributedUniqueId";

    private static final String CTX_HEADER = "$header";
    private static final String CTX_BODY = "$body";
    private static final String CTX_QUERY = "$query";
    /**
     * 请求协议在变量上下文的标识
     */
    private static final String CTX_PROTOCOL = "$protocol";
    private static final String CTX_IP = "$ip";
    private static final String CTX_PORT = "$port";
    private static final String CTX_URL = "$url";
    /**
     * 变量上下文中「请求类型／HTTP 方法」的标识
     */
    private static final String CTX_REQ_TYPE = "$req_type";
    /**
     * 「会话／token」过期时间，可在会话登录脚本中配置
     */
    private static final String CTX_EXPIRES_IN = "$expires_in";
    private static final String CTX_REQUEST_CONTEXT = "$request_context";
    /**
     * 「变量上下文」中「后端超时时间」的标识
     */
    private static final String CTX_BACKEND_TIMEOUT = "$backend_timeout";
    private static final String CTX_SYS_TYPE = "$sys_type";
    private static final String CTX_BACKEND_SERVER_TYPE = "$backend_server_type";
    private static final String CTX_BACKEND_DISTRIBUTE_STRATEGY = "$backend_distribute_strategy";

    /**
     * 苍穹分布式缓存
     */
    private static final DistributeSessionlessCache CACHES
            = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("IIR_API_THIRD_TOKEN");

    private static final String API_MISSING = "找不到对应API";
    private static final String DATASRC_MISSING = "找不到对应数据源";
    private static final String HTTP_CLIENT_METHOD_EXCEPTION = "调用Http客户端方法出现异常";
    private static final String ENCODE_EXCEPTION = "编码失败";
    private static final String SCRIPT_EXCEPTION = "脚本执行异常";
    private static final String HTTP_RET_NULL = "HTTP返回结果为空";

    /**
     * Don't let anyone instantiate this class
     */
    private LocalApiHelper() {
    }

    /**
     * 主导企业入口
     *
     * @param deid 主导企业 ID
     * @return 请求方所需 JSON 对象
     */
    public static JSONObject getDeLocalApiParaStructure(Long deid) {
        DynamicObject deLocalApiBasicInfoDo = loadDeLocalApiBasicInfoDo(deid);
        return checkApiDyOExists(deLocalApiBasicInfoDo);
    }

    /**
     * 协同企业入口
     *
     * @param localApi 协同企业本地 API ID
     * @return 请求方所需 JSON 对象
     */
    public static JSONObject getCeLocalApiParaStructure(Long localApi) {
        DynamicObject ceLocalApiBasicInfoDo = loadCeLocalApiBasicInfoDo(localApi);
        return checkApiDyOExists(ceLocalApiBasicInfoDo);
    }

    /**
     * 检查本地 API 与 API 数据源的状况
     *
     * @param localApiBasicInfoDo 本地 API 基本信息 DyO
     * @return
     */
    private static JSONObject checkApiDyOExists(DynamicObject localApiBasicInfoDo) {
        // 未找到对应本地API
        if (null == localApiBasicInfoDo) {
            logger.warn(API_MISSING);
            return new JSONObject();
        }
        // API数据源拿到的是「会话登录脚本」
        DynamicObject dataSrcDyO = ApiDataSrcHelper
                .getFullDataSrcInfo(localApiBasicInfoDo.getLong(CommonConstants.GROUP_ID));
        // 未找到对应API数据源
        if (dataSrcDyO == null) {
            logger.warn(DATASRC_MISSING);
            return new JSONObject();
        }
        return buildApiRequestPara(localApiBasicInfoDo, dataSrcDyO);
    }

    /**
     * 构建 API 数据源变量上下文
     *
     * @param dataSrcDyO 数据源 DyO
     * @return {@code ctx} - API 数据源变量上下文
     */
    private static Map<String, Object> buildDataSrcVarCtx(DynamicObject dataSrcDyO) {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put(CTX_PROTOCOL, dataSrcDyO.get(REQ_PROTOCOL));
        ctx.put(CTX_IP, dataSrcDyO.get(SERVER_IP));
        ctx.put(CTX_BACKEND_SERVER_TYPE, dataSrcDyO.get("backservtype"));
        ctx.put(CTX_BACKEND_DISTRIBUTE_STRATEGY, dataSrcDyO.get("backdistrstra"));
        ctx.put(CTX_PORT, dataSrcDyO.get(PORT));
        ctx.put(CTX_BACKEND_TIMEOUT, dataSrcDyO.get("backtimedout"));
        ctx.put(CTX_EXPIRES_IN, null);
        ctx.put(CTX_REQUEST_CONTEXT, RequestContext.get());
        return ctx;
    }

    /**
     * 检查会话登录脚本
     *
     * @param dataSrcDyO 数据源 DyO
     * @return token JSON 字符串
     */
    private static Map<String, String> checkSessionScript(DynamicObject dataSrcDyO, Map<String, Object> varCtx) {
        // 会话登录脚本
        String loginScriptTag = (String) dataSrcDyO.get(ApiDataSrcConstants.LOGIN_SCRIPT_TAG);
        long dataSrcDyOId = dataSrcDyO.getLong(CommonConstants.ID);
        String secAuth = (String) dataSrcDyO.get(SEC_AUTH);
        // 「安全认证」选择了非「无」的选项，且配置了脚本则执行，否则不执行
        if (!Objects.equals(secAuth, "NONE") && !StringUtils.isEmpty(loginScriptTag)) {
            return executeSessionScript(dataSrcDyOId, loginScriptTag, varCtx);
        } else {
            return Collections.emptyMap();
        }
    }
	
	/**
     * 执行会话脚本并获取 token
     *
     * @param loginScriptTag 会话登录脚本
     * @param varCtx         上下文变量
     * @return 执行会话脚本后返回的 json 格式的 token 字符串
     */
    private static Map<String, String> executeSessionScript(Long dataSrcDyOId, String loginScriptTag,
                                                            Map<String, Object> varCtx) {
        Map<String, String> cacheMap = new HashMap<>(2);
        String tenantId = RequestContext.get().getTenantId();
        String accountId = RequestContext.get().getAccountId();
        // 分布式会话登录唯一ID = 租户ID + 数据中心ID + 数据源ID
        String distributedUniqueId = tenantId + "-" + accountId + "-" + dataSrcDyOId;
        cacheMap.put(DISTRIBUTED_UNIQUE_ID, distributedUniqueId);
        logger.info("distributed_unique_id=" + distributedUniqueId);
        if (CACHES.get(distributedUniqueId) == null || CACHES.get(distributedUniqueId).isEmpty()) {
            try {
                OpenApiScript openApiRet = OpenApiScript.compile(loginScriptTag, varCtx);
                Object eval = openApiRet.eval(varCtx);
                Integer scriptExpiresTime = (Integer) varCtx.get(CTX_EXPIRES_IN);
                logger.info(eval.toString());
                String token = eval.toString();
                logger.info("Caches status=new");
                // 用户在脚本里配置了过期时间，则以脚本为准；否则按照苍穹默认过期时间
                if (scriptExpiresTime != null) {
                    logger.info("script_expires_time=" + scriptExpiresTime);
                }
                cacheMap.put(TOKEN_KEY, token);
            } catch (KDException e) {
                logger.error(SCRIPT_EXCEPTION);
                throw new KDException(new ErrorCode("1001", SCRIPT_EXCEPTION), e);
            }
        } else {
            logger.info("Caches exist status=" + CACHES.contains(distributedUniqueId)
                    + ", and caches value is " + CACHES.get(distributedUniqueId));
            cacheMap.put(TOKEN_KEY, CACHES.get(distributedUniqueId));
        }
        return cacheMap;
    }

    /**
     * 构建 API 请求参数
     *
     * @param localApiBasicInfoDo 本地 API 基本信息 DyO
     * @param dataSrcDyO          数据源 DyO
     * @return 解析后的数据
     */
    private static JSONObject buildApiRequestPara(DynamicObject localApiBasicInfoDo, DynamicObject dataSrcDyO) {
        // 构建变量上下文
        Map<String, Object> varCtx = buildDataSrcVarCtx(dataSrcDyO);
        // 构建最终请求的header（泛型类型不得修改）
        Map<String, String> headerMap = new HashMap<>();
        // 通过执行session脚本，或者从缓存，来获取token
        Map<String, String> tokenMap = checkSessionScript(dataSrcDyO, varCtx);
        buildHeaderMap(tokenMap, varCtx, headerMap);
        varCtx.put(CTX_URL, (buildApiUrl(localApiBasicInfoDo, dataSrcDyO)));
        varCtx.put(CTX_REQ_TYPE, localApiBasicInfoDo.getString(LocalApiConstants.REQ_METHOD));
        varCtx.put(CTX_SYS_TYPE, localApiBasicInfoDo.get(LocalApiConstants.SYS_TYPE));
        varCtx.put(CTX_HEADER, getApiReqHeader(localApiBasicInfoDo));
        if (localApiBasicInfoDo.get(LocalApiConstants.REQ_METHOD).equals(HttpConstants.METHOD_POST)) {
            Map<String, Object> apiReqBody = getApiReqBody(localApiBasicInfoDo);
            varCtx.put(CTX_BODY, apiReqBody);
        }
        varCtx.put(CTX_QUERY, getApiQueryParams(localApiBasicInfoDo));
        String preExeScriptTag = (String) localApiBasicInfoDo.get(LocalApiConstants.PREEXE_SCRIPT_TAG);
        // 由于调用HttpClientUtils的get和post方法中，body的「类型」不一样，所以这里body对象也要区分一下
        Map<String, Object> getMethodBodyMap = new HashMap<>();
        JSONObject postMethodBodyJson = new JSONObject();
        if (varCtx.get(CTX_BODY) != null) {
            getMethodBodyMap.putAll((Map<String, ?>) varCtx.get(CTX_BODY));
            postMethodBodyJson.putAll((Map<String, ?>) varCtx.get(CTX_BODY));
        }
        if (varCtx.get(CTX_HEADER) != null) {
            headerMap.putAll((Map<String, String>) varCtx.get(CTX_HEADER));
        }
        // 如果前置脚本有内容，则执行，最终发送的请求头、请求体、和Query参数要以脚本修改后的为准。
        if (preExeScriptTag != null && preExeScriptTag.length() != 0) {
            preExeScriptExecutor(preExeScriptTag, varCtx);
            if (varCtx.get(CTX_BODY) != null) {
                postMethodBodyJson.putAll((Map<String, String>) varCtx.get(CTX_BODY));
            }
            headerMap.putAll((Map<String, String>) varCtx.get(CTX_HEADER));
        }
        Map<String, String> ctxQueryMap = (Map<String, String>) varCtx.get(CTX_QUERY);
        return invokeHttpClientsMethods(localApiBasicInfoDo, varCtx, headerMap, getMethodBodyMap,
                postMethodBodyJson, ctxQueryMap, tokenMap);
    }

    /**
     * 构建请求头映射
     *
     * @param tokenMap  Token 映射
     * @param varCtx    变量上下文
     * @param headerMap 请求头映射
     */
    private static void buildHeaderMap(Map<String, String> tokenMap, Map<String, Object> varCtx,
                                       Map<String, String> headerMap) {
        if (tokenMap != null && !tokenMap.isEmpty()) {
            logger.info("token=" + tokenMap);
            logger.info("expires_in=" + varCtx.get(CTX_EXPIRES_IN));
            JSONObject jsonObject = JSON.parseObject(tokenMap.get(TOKEN_KEY));
            headerMap.putAll((Map<String, String>) jsonObject.get(HttpConstants.HEADER));
        }
    }
	
	/**
     * 调用 HTTP 客户端方法
     *
     * @param localApiBasicInfoDo 本地API基本信息
     * @return 调用 HTTP 客户端方法后返回的 JSON
     */
    private static JSONObject invokeHttpClientsMethods(DynamicObject localApiBasicInfoDo,
                                                       Map<String, Object> varCtx,
                                                       Map<String, String> headerMap,
                                                       Map<String, Object> getMethodBodyMap,
                                                       JSONObject postMethodBodyJson,
                                                       Map<String, String> ctxQueryMap,
                                                       Map<String, String> tokenMap) {
        // 构建面板上的Query参数
        String finalUrl = buildFinalUrl(varCtx, ctxQueryMap);
        // 如需查看视觉舒适的请求体json了，请alt+F8这里的postMethodBodyJson
        String postMethodBody = JSON.toJSONString(postMethodBodyJson);
        // connTimeout是有用的参数，
        int connTimeout = (int) varCtx.get(CTX_BACKEND_TIMEOUT);
        // 而readTimeout用处不大
        int readTimeout = 8888;
        String resultStr = null;
        String requestType = (String) varCtx.get(CTX_REQ_TYPE);
        logger.info("header_map=" + headerMap);
        logger.info("post_method_body=" + postMethodBody);
        logger.info("req_url=" + finalUrl);
        try {
            switch (requestType) {
                case HttpConstants.METHOD_GET:
                    resultStr = HttpClientUtils.get(finalUrl, headerMap, getMethodBodyMap, connTimeout, readTimeout);
                    logger.info("result_str=" + resultStr);
                    break;
                case HttpConstants.METHOD_POST:
                    resultStr = HttpClientUtils.postjson(finalUrl, headerMap, postMethodBody, connTimeout, readTimeout);
                    logger.info("result_str=" + resultStr);
                    break;
                default:
            }
        } catch (IOException e) {
            throw new KDException(new ErrorCode("1001", HTTP_CLIENT_METHOD_EXCEPTION), e);
        }
        JSONObject resultStrJson = new JSONObject();
        if (resultStr == null || resultStr.isEmpty()) {
            logger.error(HTTP_RET_NULL);
            resultStrJson.put("wrongMsg", "返回值为空，请检查请求配置是否有误");
            return resultStrJson;
        } else if (resultStr.contains("<html>")) {
            logger.error(HTTP_CLIENT_METHOD_EXCEPTION);
            resultStrJson.put("wrongMsg", "获取Web信息有误，请检查请求配置是否有误");
            return resultStrJson;
        }
        if (JSON.parse(resultStr) instanceof JSONArray) {
            JSONArray parse = (JSONArray) JSON.parse(resultStr);
            JSONObject simpleJsonObject = null;
            for (Object jsonObjectInJsonArray : parse) {
                simpleJsonObject = buildApiResponseParamDo(localApiBasicInfoDo,
                        (JSONObject) jsonObjectInJsonArray);
            }
            return simpleJsonObject;
        }
        resultStrJson = JSON.parseObject(resultStr);
        JSONObject jsonObject = buildApiResponseParamDo(localApiBasicInfoDo, resultStrJson);
        cachesSaving(varCtx, tokenMap);
        return jsonObject;
    }

    /**
     * 存储缓存
     *
     * @param varCtx   变量上下文
     * @param tokenMap Token 映射
     */
    private static void cachesSaving(Map<String, Object> varCtx, Map<String, String> tokenMap) {
        String distributedUniqueId = tokenMap.get(DISTRIBUTED_UNIQUE_ID);
        String token = tokenMap.get(TOKEN_KEY);
        // 用户在脚本里配置了过期时间，则以脚本为准；否则按照苍穹默认过期时间
        if (varCtx.get(CTX_EXPIRES_IN) != null) {
            logger.info("script_expires_time=" + varCtx.get(CTX_EXPIRES_IN));
            CACHES.put(distributedUniqueId, token, (Integer) varCtx.get(CTX_EXPIRES_IN));
            logger.info("Caches stored successful, distributed_unique_id=" + distributedUniqueId + ", "
                    + "token=" + token);
        }
    }

    /**
     * 构建 API 响应参数
     *
     * @param localApiBasicInfoDo 本地 API 基本信息 DyO
     * @param httpRetJson         调用远程接口后获取的 JSON 数据，比如可能会包括 data、errorCode、message、status 等。
     * @return 返回给调用方的 {@code JSONObject} 对象
     */
    private static JSONObject buildApiResponseParamDo(DynamicObject localApiBasicInfoDo, JSONObject httpRetJson) {
        // 初始化平铺JSON
        JSONObject flattenedJson = new JSONObject();
        // 获取基础资料页面本地API的返回参数的结构（仅结构，如 key 的信息，但是还没有 value）
        Map<String, Object> respParamStruct = getApiRespBody(localApiBasicInfoDo);
        if (respParamStruct.size() == 0) {
            return new JSONObject();
        } else {
            getFlattenedJson(httpRetJson, flattenedJson, respParamStruct);
        }
        DynamicObjectCollection apiRespBody = localApiBasicInfoDo.getDynamicObjectCollection(RESP_ENTRY_ENTITY);
        checkAllOnOrOffAndWhichToBeRemoved(apiRespBody, flattenedJson);
        JSONObject retObj = new JSONObject();
        for (DynamicObject dynamicObject : apiRespBody) {
            retObj.put(dynamicObject.getString(LocalApiConstants.RESP_PARAM_NAME),
                    flattenedJson.get(dynamicObject.getString(LocalApiConstants.RESP_PARAM_NAME)));
        }
        return retObj;
    }

    /**
     * 检测开关是否全开或全关；如果符合情况则都视为返回；如果不符合则取决于具体按钮的开关
     *
     * @param apiRespBody
     * @param flattenedJson
     */
    private static void checkAllOnOrOffAndWhichToBeRemoved(DynamicObjectCollection apiRespBody, JSONObject flattenedJson) {
        Set<Object> allOnOrOff = new HashSet<>();
        apiRespBody.forEach(dynamicObject -> allOnOrOff.add(dynamicObject.get(LocalApiConstants.RESP_PARAM_IS_RETURN)));
        if (allOnOrOff.size() <= 1) {
            return;
        }
        for (DynamicObject dynamicObject : apiRespBody) {
            String o = (String) dynamicObject.get(LocalApiConstants.RESP_PARAM_NAME);
            if (dynamicObject.get(LocalApiConstants.RESP_PARAM_IS_RETURN).equals(false)) {
                logger.info("not return resp name=" + o);
                flattenedJson.remove(o);
            }
        }
    }

    /**
     * 若遇到多层JSON，则压缩为一层给解析调用方
     *
     * @param jsonNeedToBeFlattened 源数据
     * @param flattenedJson         压缩后的数据
     * @param respParamStruct       DyO 获取的返回参数结构
     */
    private static void getFlattenedJson(Object jsonNeedToBeFlattened, JSONObject flattenedJson,
                                         Map<String, Object> respParamStruct) {
        respParamStruct.forEach((k, v) -> {
            if (jsonNeedToBeFlattened instanceof JSONObject && ((JSONObject) jsonNeedToBeFlattened).containsKey(k)) {
                Object valueOfJsonNeedToBeFlattened = ((JSONObject) jsonNeedToBeFlattened).get(k);
                if (valueOfJsonNeedToBeFlattened instanceof JSONObject) {
                    Map<String, Object> paramMap = new HashMap<>(16);
                    paramMap.putAll((Map<? extends String, ?>) valueOfJsonNeedToBeFlattened);
                    getFlattenedJson(valueOfJsonNeedToBeFlattened, flattenedJson, paramMap);
                } else if (valueOfJsonNeedToBeFlattened instanceof JSONArray) {
                    flattenJsonArray(valueOfJsonNeedToBeFlattened, flattenedJson, jsonNeedToBeFlattened);
                } else {
                    flattenedJson.put(k, ((JSONObject) jsonNeedToBeFlattened).get(k));
                }
            } else if (jsonNeedToBeFlattened instanceof JSONArray) {
                ((JSONArray) jsonNeedToBeFlattened).forEach(arr -> getFlattenedJson(arr, flattenedJson, respParamStruct));
            }
        });
    }
	
	/**
     * 数组类型 JSON 的扁平化方法
     *
     * @param valueOfJsonNeedToBeFlattened 需要被扁平的 JSON 的值
     * @param flattenedJson                扁平化后的 JSON
     * @param jsonNeedToBeFlattened        需要被扁平的 JSON
     */
    private static void flattenJsonArray(Object valueOfJsonNeedToBeFlattened, JSONObject flattenedJson,
                                         Object jsonNeedToBeFlattened) {
        JSONObject newFlattenedJson = new JSONObject();
        Map<String, Object> paramMap = new HashMap<>(16);
        for (Object jsonObject : ((JSONArray) valueOfJsonNeedToBeFlattened)) {
            if (jsonObject instanceof JSONObject) {
                // 遍历当前 JSONObject 的每一个键
                for (String key : ((JSONObject) jsonObject).keySet()) {
                    // 获取当前键的值
                    Object val = ((JSONObject) jsonObject).get(key);
                    newFlattenedJson.merge(key, val, (a, b) -> a + "; " + b);
                    flattenedJson.putAll(newFlattenedJson);
                }
            }
        }
        paramMap.putAll((Map<? extends String, ?>) jsonNeedToBeFlattened);
        // 递归调用
        getFlattenedJson(valueOfJsonNeedToBeFlattened, flattenedJson, paramMap);
    }

    /**
     * 构建最终请求 URL
     *
     * @param varCtx      变量上下文
     * @param ctxQueryMap 上下文的 Query 参数映射
     * @return 最终请求 URL
     */
    private static String buildFinalUrl(Map<String, Object> varCtx, Map<String, String> ctxQueryMap) {
        // 构建面板上的Query参数
        String finalUrl = (String) varCtx.get(CTX_URL);
        if (ctxQueryMap != null && ctxQueryMap.size() != 0) {
            String queryStr = buildApiQueryParamString(ctxQueryMap);
            finalUrl = finalUrl + "?" + queryStr;
            logger.info(queryStr);
        }
        return finalUrl;
    }

    /**
     * 构建完整的 URL 地址。包括协议、主机地址、端口号、路径。但请注意不包含 query 参数。
     * <br>
     * 因为脚本还有可能要对 query 参数进行修改，所以在最后调用之前再拼接 query 参数
     *
     * @param deLocalApiBasicInfoDo 主导企业本地 API 对象
     * @return 完整的 URL 地址
     */
    private static String buildApiUrl(DynamicObject deLocalApiBasicInfoDo, DynamicObject dataSrcDo) {
        StringBuilder url = new StringBuilder();
        url.append(dataSrcDo.getString(ApiDataSrcConstants.REQ_PROTOCOL));
        url.append("://");
        url.append(dataSrcDo.getString(ApiDataSrcConstants.SERVER_IP));
        if (StringUtils.isNotEmpty(dataSrcDo.getString(ApiDataSrcConstants.PORT))) {
            url.append(":");
            url.append(dataSrcDo.getString(ApiDataSrcConstants.PORT));
        }
        String reqPath = deLocalApiBasicInfoDo.getString(LocalApiConstants.REQ_PATH);
        if (!reqPath.startsWith("/")) {
            url.append("/").append(reqPath);
        } else {
            url.append(reqPath);
        }
        return url.toString();
    }

    /**
     * 前置脚本执行方法
     *
     * @param preExeScriptTag 前置脚本
     * @return
     */
    private static void preExeScriptExecutor(String preExeScriptTag, Map<String, Object> apiCtx) {
        try {
            OpenApiScript openApiRet = OpenApiScript.compile(preExeScriptTag, apiCtx);
            openApiRet.eval(apiCtx);
        } catch (KDException e) {
            logger.error(SCRIPT_EXCEPTION);
            throw new KDException(new ErrorCode("1002", SCRIPT_EXCEPTION), e);
        }
    }

    /**
     * 构建 Query 参数 URL 子串，并对参数中的中文进行转义
     *
     * @param queryParamMap Query 参数映射对象
     * @return Query 参数字符串
     */
    private static String buildApiQueryParamString(Map<String, String> queryParamMap) {
        StringBuilder ret = new StringBuilder();
        queryParamMap.forEach((key, val) -> {
            try {
                key = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
                val = URLEncoder.encode(val, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                throw new KDException(new ErrorCode("1005", ENCODE_EXCEPTION), e);
            }
            ret.append(key).append("=").append(val).append("&");
        });
        ret.deleteCharAt(ret.length() - 1);
        return ret.toString();
    }

    /**
     * 加载主导企业 API 基础信息 DyO
     *
     * @param deid 主导企业 ID
     * @return 主导企业 API 基础信息 DyO
     */
    private static DynamicObject loadDeLocalApiBasicInfoDo(Long deid) {
        QFilter qFilter = new QFilter(EntRelaConstant.DE_ID, QCP.equals, deid);
        return BusinessDataServiceHelper.loadSingle(LocalApiConstants.IIIRS_LOCALAPI, qFilter.toArray());
    }
	
	/**
     * 加载协同企业 API 基础信息 DyO
     *
     * @param localApiId 协同企业 API ID
     * @return 主导企业 API 基础信息 DyO
     */
    private static DynamicObject loadCeLocalApiBasicInfoDo(Long localApiId) {
        QFilter qFilter = new QFilter(CommonConstants.ID, QCP.equals, localApiId);
        return BusinessDataServiceHelper.loadSingle(LocalApiConstants.IIIRS_LOCALAPI, qFilter.toArray());
    }

    /**
     * 获取元数据面板上的 API 请求头
     *
     * @param localApiBasicInfoDo 本地 API 基础信息 DyO
     * @return 请求头映射
     */
    public static Map<String, String> getApiReqHeader(DynamicObject localApiBasicInfoDo) {
        DynamicObjectCollection headerentryentitys
                = localApiBasicInfoDo.getDynamicObjectCollection(HEADER_ENTRY_ENTITY);
        Map<String, String> headerMap = new HashMap<>();
        for (DynamicObject headerEntryEntity : headerentryentitys) {
            headerMap.put(headerEntryEntity.getString(LocalApiConstants.HEADER_NAME),
                    headerEntryEntity.getString(LocalApiConstants.HEADER_VALUE));
        }
        return headerMap;
    }

    /**
     * 获取 API 请求 Query 参数
     *
     * @param localApiBasicInfoDo 本地 API 基本信息 DyO
     * @return Query 参数映射集
     */
    private static Map<String, String> getApiQueryParams(DynamicObject localApiBasicInfoDo) {
        DynamicObjectCollection queryEntryEntities = localApiBasicInfoDo.getDynamicObjectCollection(QUERY_ENTRY_ENTITY);
        Map<String, String> queryMap = new HashMap<>();
        for (DynamicObject queryEntryEntity : queryEntryEntities) {
            queryMap.put(queryEntryEntity.getString(LocalApiConstants.QUERY_PARA_NAME),
                    queryEntryEntity.getString(LocalApiConstants.QUERY_PARA_SAMPLE));
        }
        return queryMap;
    }

    /**
     * 获取 API 请求体
     *
     * @param localApiBasicInfoDo
     * @return
     */
    private static Map<String, Object> getApiReqBody(DynamicObject localApiBasicInfoDo) {
        DynamicObjectCollection reqBodyEntryCollection
                = localApiBasicInfoDo.getDynamicObjectCollection(BODY_ENTRY_ENTITY);
        return IiirsCustomApiUtils.getCustomJsonParams(BODY_ENTRY_ENTITY, reqBodyEntryCollection);
    }

    /**
     * 获取 API 返回体
     *
     * @param localApiBasicInfoDo
     * @return
     */
    private static Map<String, Object> getApiRespBody(DynamicObject localApiBasicInfoDo) {
        DynamicObjectCollection apiRespBody = localApiBasicInfoDo.getDynamicObjectCollection(RESP_ENTRY_ENTITY);
        return IiirsCustomApiUtils.getCustomJsonParams(RESP_ENTRY_ENTITY, apiRespBody);
    }
}