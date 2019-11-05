package org.test.records.util.retresult;

/**
 * @Description: 返回对象实体
 * @author
 * @date 2018/4/19 09:43
 */
public class RetResponse<T> {

    private final static String SUCCESS = "success";
    private final static String FAIL = "fail";

    public static <T> RetResult<T> makeOKRsp(T data) {
        return new RetResult<T>().setCode(RetCode.SUCCESS).setMsg(SUCCESS).setData(data);
    }

    public static <T> RetResult<T> makeErrRsp(T data) {
        return new RetResult<T>().setCode(RetCode.FAIL).setMsg(FAIL).setData(data);
    }

    public static <T> RetResult<T> makeRsp(int code, String msg) {
        return new RetResult<T>().setCode(code).setMsg(msg);
    }

    public static <T> RetResult<T> makeRsp(int code, String msg, T data) {
        return new RetResult<T>().setCode(code).setMsg(msg).setData(data);
    }

}