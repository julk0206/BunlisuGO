package bunlisugo.server.controller;

public interface ClientCommandHandler {
	void handle(String[] parts, GameClientHandler session);
}
// String[] parts: 클라이언트가 서버로 보낸 메시지를 | 기준으로 split해 만든 문자열 배열
// GameClientHandler session: 이 명령을 보낸 클라이언트의 "세션" 객체