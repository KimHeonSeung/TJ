// package 
// 소스코드를 관리하기 위한 방법을 제공하는 문법
// 각각의 소스코드들을 서로 다른 디렉토리로 저장하는 방법
// 만약 패키지를 사용하여 클래스를 선언하는 경우
// 반드시 해당 소스코드의 첫번째 라인은 
// package 선언문이 작성되야 합니다.
// package 현재디렉토리명;
package package_1;

// 패키지를 사용하여 클래스를 작성하는 경우
// 보편적인 이름의 클래스라도 서로 다른 패키지에 
// 위치하는 경우 중복된 이름을 사용할 수 있습니다.

// 패키지명은 폴더 이름이 됩니다.
// 패키지 명은 URL 을 역순으로 지정하는 경우가 많습니다.
// - 겹치지 않는 값을 같기 위해서 도메인이름을 사용합니다
// EX) kr.co.tjoeun

public class Package_01 {

	public static void main(String[] args) {
		java.lang.System.out.println("패키지 명을 사용한 클래스 접근");
	}

}
