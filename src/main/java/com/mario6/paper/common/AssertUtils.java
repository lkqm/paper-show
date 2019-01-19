package com.mario6.paper.common;

/**
 * AssertUtils
 *
 * @author Mario Luo
 * @date 2019.01.19 11:34
 *
 * @throws ServiceException 参数校验失败抛出异常
 */
public class AssertUtils {


    /**
     * 参数校验
     * @param expression 期望值
     * @param message 不满足期望值的消息
     */
    public static void checkParam(boolean expression, String message, Object...msgParams) {
        if(!expression) {
            throw new ServiceException(message);
        }
    }

    public static void checkParamNotNull(Object obj, String message, Object...msgParams) {
        if(obj == null) {
            throw new ServiceException(message);
        }
    }
}
