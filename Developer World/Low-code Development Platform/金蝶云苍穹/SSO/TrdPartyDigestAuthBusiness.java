package kd.iir.iiirs.business.auth;

import kd.bos.cache.CacheFactory;
import kd.bos.cache.DistributeSessionlessCache;
import kd.bos.servicehelper.user.UserServiceHelper;
import kd.iir.iiirs.common.utils.uuid.UuidGeneratorUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wb_sun_jincheng
 * @version 1.0
 * @project iir-iiirs-business
 * @description
 * @date 13:54:45 2024/08/12
 */
public class TrdPartyDigestAuthBusiness {

    /**
     * 苍穹分布式缓存
     */
    private static final DistributeSessionlessCache CACHES
            = CacheFactory.getCommonCacheFactory().getDistributeSessionlessCache("IIR_THIRD_DIGEST_AUTH");
    /**
     * 登录随机码 code 有效期（单位：s）
     */
    private static final Integer CODE_TIMEOUT_SECONDS = 120;

    /**
     * Don't let anyone instantiate this class
     */
    private TrdPartyDigestAuthBusiness() {
    }

    /**
     * 获取 32 位登录随机码
     *
     * @return 32 位登录随机码
     */
    public static String getLoginCode(String phoneNumber, String userId) {
        String pisLoginRandomCode = UuidGeneratorUtils.getPisLoginRandomCode();
        Map<String, String> userInfoMapping = new HashMap<>();
        userInfoMapping.put("userId", userId);
        userInfoMapping.put("phone", phoneNumber);
        CACHES.put(pisLoginRandomCode, userInfoMapping, CODE_TIMEOUT_SECONDS);
        return pisLoginRandomCode;
    }

    /**
     * 通过授权码 code 获取用户信息
     *
     * @param code 授权码
     * @return 用户信息映射对象
     */
    public static Map<String, Object> getUserInfoByCode(String code) {
        Map<String, String> userInfoMapping = CACHES.getAll(code);
        long userId = Long.parseLong(userInfoMapping.get("userId"));
        return UserServiceHelper.getUserInfoByID(userId);
    }
}