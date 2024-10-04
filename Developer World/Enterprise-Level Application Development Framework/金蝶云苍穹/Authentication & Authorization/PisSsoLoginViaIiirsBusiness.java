package kd.iir.pis.business.auth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import kd.iir.pis.common.constant.PisCommonConstants;
import kd.iir.pis.common.utils.SysParamUtil;
import kd.bos.dc.api.model.Account;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.login.thirdauth.app.AppAuthResult;
import kd.bos.login.thirdauth.app.ThirdAppAuthtication;
import kd.bos.login.thirdauth.app.UserType;
import kd.bos.login.utils.ShaSignUtils;
import kd.bos.util.HttpClientUtils;
import kd.iir.pis.business.helper.PisAddUserByIiirsHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * @author sun_jincheng
 * @version 1.0
 * @project IIR-PIS
 * @description 借助 IIIRS 认证免密登录 PIS 插件
 * @date 10:09:15 2024/08/13
 */
public class PisSsoLoginViaIiirsBusiness extends ThirdAppAuthtication {

    /**
     * 密钥
     */
    private static final String APP_SECRET = "appsecret";
    /**
     * 授权 APP
     */
    private static final String AUTH_APP = "authApp";
    /**
     * 授权 APP ID
     */
    private static final String AUTH_APP_ID = "authappid";
    /**
     * IIIRS 获取用户信息地址
     */
    private static final String IIIRS_USER_INFO_URL = "iiirsuserinfourl";
    /**
     * 32 位授权随机码
     */
    private static final String CODE = "code";
    private static final String USER = "user";
    /**
     * 日志
     */
    private static final Log logger = LogFactory.getLog(PisSsoLoginViaIiirsBusiness.class);

    /**
     * 检查请求 URL 是否需要本插件认证。
     *
     * @param request       当前访问的 {@code HttpServletRequest} 对象
     * @param currentCenter 当前访问的数据中心
     * @return 如果是则返回 {@code true}，否则返回 {@code false}
     */
    @Override
    public boolean isNeedHandle(HttpServletRequest request, Account currentCenter) {
        boolean isNeed = false;
        // HttpServletRequest.getParameter方法可以直接获取URL的query参数
        // 获取授权应用
        String authApp = request.getParameter(AUTH_APP);
        // 获取授权码
        String code = request.getParameter(CODE);
        if (authApp != null && authApp.equals(PisCommonConstants.IIIRS) && code != null && !code.isEmpty()) {
            isNeed = true;
        }
        // 这里如果为true，那么将会继续执行appAuthtication方法
        return isNeed;
    }

    /**
     * 实现 PIS 调用 IIIRS 相关接口完成认证，返回认证结果。
     *
     * @param request       {@code HttpServletRequest} 对象
     * @param currentCenter 当前访问的数据中心
     * @return 认证结果 {@code AppAuthResult} 对象，包含：{@code succeed}、{@code userType}、{@code userFlag} 等对象属性
     */
    @Override
    public AppAuthResult appAuthtication(HttpServletRequest request, Account currentCenter) {
        Map<String, Object> sysParams = SysParamUtil.getSysParams();
        AppAuthResult authResult = new AppAuthResult();
        authResult.setSucceed(false);
        // 从请求 URL 里直接截取 query 参数 code
        String loginRandomCode = request.getParameter(CODE);
        if (loginRandomCode == null || loginRandomCode.isEmpty()) {
            logger.error("未获取到认证码，免密登录失败");
            return authResult;
        }
        // 用 code 换取用户信息
        String userInfoByCode = getUserInfoByCode(request, currentCenter, sysParams);
        JSONObject retJson = JSON.parseObject(userInfoByCode);
        Map<String, String> userInfo = (Map<String, String>) retJson.get("data");
        if (userInfo != null) {
            authResult.setSucceed(true);
            authResult.setUserFlag(userInfo.get(PisCommonConstants.PHONE));
            authResult.setUserType(UserType.MOBILE_PHONE);
            PisAddUserByIiirsHelper.syncUserToPis(userInfo);
        }
        return authResult;
    }

    /**
     * PIS 调用 IIIRS API，用 code 换取获取用户信息
     *
     * @param request       {@code HttpServletRequest} 对象
     * @param currentCenter 当前访问的数据中心
     * @param sysParams     苍穹应用参数
     * @return 用户信息
     */
    private static String getUserInfoByCode(HttpServletRequest request, Account currentCenter,
                                            Map<String, Object> sysParams) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String signatureNonce = UUID.randomUUID().toString();
        String code = request.getParameter(CODE);
        String appSecret = (String) sysParams.get(APP_SECRET);
        String signatureString = getDigest(appSecret, timeStamp, signatureNonce, code);
        // 构建最终换取用户信息的请求 URL
        String finalUrl = buildRequestUrl(request, sysParams, signatureString,
                currentCenter, timeStamp, signatureNonce);
        String userInfoStr;
        try {
            userInfoStr = HttpClientUtils.get(finalUrl);
        } catch (Exception e) {
            logger.error("调用IIIRS认证接口失败");
            return "";
        }
        if (userInfoStr.contains("格式错误")) {
            logger.error("认证参数格式出现错误，请检查");
        } else if (userInfoStr.contains("\"errorCode\":\"400\"")) {
            logger.error("认证参数有误，认证不通过，请检查");
        }
        return userInfoStr;
    }

    /**
     * 构建用 code 换取用户信息的请求 URL
     *
     * @param request         {@code HttpServletRequest} 对象
     * @param sysParams       PIS 应用参数
     * @param signatureString 加密后的签名
     * @param currentCenter   数据中心
     * @param timeStamp       时间戳
     * @param signatureNonce  签名 Nonce
     * @return 用 code 换取用户信息的请求 URL
     */
    private static String buildRequestUrl(HttpServletRequest request, Map<String, Object> sysParams,
                                          String signatureString, Account currentCenter, String timeStamp,
                                          String signatureNonce) {
        // PIS 应用参数里定义的授权 appid
        String appId = (String) sysParams.get(AUTH_APP_ID);
        // IIIRS 获取用户信息接口
        String iiirsGetUserInfoUrl = (String) sysParams.get(IIIRS_USER_INFO_URL);
        String code = CODE + "=" + request.getParameter(CODE);
        StringBuilder sb = new StringBuilder(iiirsGetUserInfoUrl);
        String accountId = currentCenter.getAccountId();
        String userName = request.getParameter(USER);
        sb.append("?")
                .append("appId=").append(appId)
                .append("&timestamp=").append(timeStamp)
                .append("&signatureNonce=").append(signatureNonce)
                .append("&signature=").append(signatureString)
                .append("&parameters=code")
                .append("&user=").append(userName)
                .append("&usertype=Mobile")
                .append("&accountId=").append(accountId)
                .append("&").append(code);
        return sb.toString();
    }

    /**
     * 获取摘要
     *
     * @param appSecret      APP 密钥
     * @param code           授权随机码 code
     * @param timeStamp      时间戳
     * @param signatureNonce 参与加密的随机码
     * @return 签名 signature
     */
    private static String getDigest(String appSecret, String timeStamp, String signatureNonce, String code) {
        // 根据公式，参与加密的参数样式是这样的：
        // code=77b508fcf4f44a9fba26b348c4e0cb6f1724914220617e7ce89d8-f041-496d-8235-40a92cf656e4
        // 也就是直接把 code=77b508fcf4f44a9fba26b348c4e0cb6f（加密参数）、1724914220617（时间戳）、以及
        // e7ce89d8-f041-496d-8235-40a92cf656e4（Nonce）机械地拼接起来，无数次试错后这样拼接就不错了
        String signatureString = getSignatureString(timeStamp, signatureNonce, code);
        try {
            return ShaSignUtils.HMACSHA256StrByKey(signatureString, appSecret);
        } catch (Exception e) {
            logger.error("获取加密算法失败，无法进行认证／无效的密钥，请联系系统管理人员");
            return "";
        }
    }

    /**
     * 获取加密前的签名
     *
     * @param timeStamp      时间戳
     * @param signatureNonce 签名 Nonce
     * @param code           授权随机码 code
     * @return 加密前的签名字符串
     */
    private static String getSignatureString(String timeStamp, String signatureNonce, String code) {
        // 注意拼接加密参数一定不能有一丁点错误，严格按照这样来拼
        return CODE + "=" + code + timeStamp + signatureNonce;
    }
}