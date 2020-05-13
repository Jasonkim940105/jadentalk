package firstpage.com;

public final class Protocol {
    public final static int LOGIN = 100; // 로그인
    public final static int LOGIN_OK = 101; // 로그인 성공
    public final static int LOGIN_NO = 102; // 로그인 실패
    public final static int LOGIN_SUCCESS = 108; // 로그인 성공후, myPage 이동
    public final static int LOGOUT = 109; // 로그아웃

    public final static int JOIN = 200; // 회원가입
    public final static int ID_CHECK = 201; //중복회원 확인
    public final static int ID_USINGNOW = 202; // 중복아이디가 있을 경우
    public final static int ID_UNUSING = 203; // 중복아이디가 없을 경우


    public final static int TEST = 300;




}
