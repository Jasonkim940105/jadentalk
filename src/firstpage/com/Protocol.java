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


    public final static int ADD_ID = 300; // 친구추가
    public final static int FRIEND_ID_NOT_EXIST = 301; //친구추가한 아이디가 없는경우
    public final static int FRIEND_REQUEST_SUCCESS = 302; //친구요청이 성공정으로 날라간 경우
    public final static int FRIEND_REQUEST_FAIL = 309; //친구요청 실패


    public final static int FRIEND_REQUEST_TO_ME = 400; // 나에게 들어온 친구요청
    public final static int FRIEND_LIST_SHOW = 401; // 나에게 들어온 친구요청을 전부 보여줌
    public final static int FRIEND_LIST_EMPTY = 402; // 나에게 들어온 친구요청이 없음






}
