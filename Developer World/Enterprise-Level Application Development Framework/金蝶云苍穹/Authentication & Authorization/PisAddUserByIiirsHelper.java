package kd.iir.pis.business.helper;

import kd.iir.pis.common.constant.PisCommonConstants;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.db.tx.TX;
import kd.bos.db.tx.TXHandle;
import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.orm.query.QCP;
import kd.bos.orm.query.QFilter;
import kd.bos.permission.model.UserParam;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.QueryServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;
import kd.bos.servicehelper.user.UserServiceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wb_sun_jincheng
 * @version 1.0
 * @project node-debug-mservice
 * @description 把 IIIRS 用户数据同步进 PIS 系统
 * @date 11:19:44 2024/08/30
 */
public class PisAddUserByIiirsHelper {

    private static final Log logger = LogFactory.getLog(PisAddUserByIiirsHelper.class);
    /**
     * 人员映射表
     */
    private static final String BAS_IM_MAPPING = "bas_immapping";
    /**
     * 即时通讯应用类型表
     */
    private static final String BAS_INSTANT_MSG_TYPE = "bas_instantmsgtype";

    private PisAddUserByIiirsHelper() {
    }

    /**
     * 把 IIIRS 用户数据同步进 PIS 系统
     *
     * @param userInfo 远程调用 IIIRS 获取的 JSON 格式用户信息再转成 Map
     */
    public static void syncUserToPis(Map<String, String> userInfo) {
        String phone = userInfo.get(PisCommonConstants.PHONE);
        boolean ifExists = QueryServiceHelper.exists(PisCommonConstants.BOS_USER,
                new QFilter[]{new QFilter(PisCommonConstants.PHONE, QCP.equals, phone)});
        if (ifExists) {
            return;
        }
        String name = userInfo.get(PisCommonConstants.NAME);
        String iiirsUserId = userInfo.get(PisCommonConstants.ID);
        String email = userInfo.get(PisCommonConstants.EMAIL);
        List<UserParam> paramList = new ArrayList<>();
        StringBuilder errMsg = new StringBuilder();
        UserParam user = new UserParam();
        // 存储返回信息
        Map<String, String> resutMap = new HashMap<>(16);
        Map<String, Object> entryEntity = new HashMap<>();
        // 职位分录
        List<Map<String, Object>> posList = new ArrayList<>();
        // 设置部门ID
        entryEntity.put("dpt", 100000L);
        entryEntity.put("position", "注册用户");
        posList.add(entryEntity);
        Map<String, Object> userDataMapping = new HashMap<>();
        userDataMapping.put(PisCommonConstants.NAME, name);
        userDataMapping.put(PisCommonConstants.USERNAME, name);
        userDataMapping.put(PisCommonConstants.PHONE, name);
        userDataMapping.put(PisCommonConstants.EMAIL, email);
        // 标识的大小写敏感，不能写错了
        userDataMapping.put("entryentity", posList);
        userDataMapping.put("isregisted", Boolean.TRUE);
        userDataMapping.put("isactived", Boolean.TRUE);
//        userDataMapping.put("password", PasswordUtil.getEncryPassword(pwd));
        user.setDataMap(userDataMapping);
        paramList.add(user);
        try (TXHandle txHandle = TX.requiresNew()) {
            UserServiceHelper.add(paramList);
            UserParam userParam = paramList.get(0);
            // 如果添加成功，userParam对象可以直接获取用户ID
            syncUserInfoToImMapping(iiirsUserId, String.valueOf(userParam.getId()));
            try {
                if (user.isSuccess()) {
                    resutMap.put("userId", String.valueOf(user.getId()));
                    logger.info(resutMap.toString());
                } else {
                    errMsg.append(user.getMsg());
                }
            } catch (Exception kdException) {
                txHandle.setRollback(true);
                txHandle.markRollback();
                errMsg.append(kdException.getMessage());
                logger.error(errMsg.toString());
            }
        }
    }

    /**
     * 同步进人员映射表 t_bas_immapping
     *
     * @param iiirsUserId IIIRS 用户 ID
     * @param pisUserId   PIS 用户 ID
     */
    private static void syncUserInfoToImMapping(String iiirsUserId, String pisUserId) {
        boolean exists = QueryServiceHelper.exists(BAS_IM_MAPPING,
                new QFilter[]{new QFilter("openid", QCP.equals, iiirsUserId)});
        if (!exists) {
            DynamicObject dynamicObject = BusinessDataServiceHelper.newDynamicObject(BAS_IM_MAPPING);
            // 内部账号ID／苍穹人员ID
            dynamicObject.set("userid", pisUserId);
            // 外部账号ID／第三方系统人员ID
            dynamicObject.set("openid", iiirsUserId);
            // 映射类型ID／即时通讯应用类型
            DynamicObject instantMsgTypeDo = BusinessDataServiceHelper.newDynamicObject(BAS_INSTANT_MSG_TYPE);
            instantMsgTypeDo.set(PisCommonConstants.ID, "2027666271595686912");
            dynamicObject.set("imtype", instantMsgTypeDo);
            SaveServiceHelper.save(new DynamicObject[]{dynamicObject});
        }
    }
}