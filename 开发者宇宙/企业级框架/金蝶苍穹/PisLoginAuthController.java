package kd.iir.iiirs.webapi;

import kd.bos.logging.Log;
import kd.bos.logging.LogFactory;
import kd.bos.openapi.common.custom.annotation.*;
import kd.bos.openapi.common.result.CustomApiResult;
import kd.iir.iiirs.business.auth.TrdPartyDigestAuthBusiness;

import java.io.Serializable;
import java.util.Map;

/**
 * @author wb_sun_jincheng
 * @version 1.0
 * @project iir-iiirs-webapi
 * @description
 * @date 13:44:06 2024/08/13
 */
@ApiController(value = "pisLoginAuthController", desc = "PIS应用调取IIIRS认证接口")
@ApiMapping(value = "iir")
public class PisLoginAuthController implements Serializable {

    private static final Log logger = LogFactory.getLog(PisLoginAuthController.class);

    @ApiGetMapping(value = "/pisLoginGetUserInfo", desc = "PIS应用调取IIIRS获取用户信息")
    public CustomApiResult<@ApiResponseBody Object> getUserInfo(@ApiParam(value = "授权码") String code) {
        Map<String, Object> userInfo = TrdPartyDigestAuthBusiness.getUserInfoByCode(code);
        return CustomApiResult.success(userInfo);
    }
}