package com.emergency.lecture.emergency.webserver;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kyeongwon on 2016-11-11.
 */

public class WebServerSignUp extends Thread {
    private static URL url;

    public AppCompatActivity appCompatActivity;
    private Context context;

    private String id;
    private String pwd;
    private String email;

    private Handler handler;

    public WebServerSignUp(AppCompatActivity appCompatActivity, Context context, String id, String pwd, String email)
    {
        super();

        try{
            this.url = new URL("http://172.30.1.8:8089/signup_do");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.appCompatActivity = appCompatActivity;
        this.context = context;
        this.id = id;
        this.pwd = pwd;
        this.email = email;
        this.handler = new Handler();
    }

    public void run()
    {
        super.run();
        String returnString = request();

        /*
        * 여기서 returnString 값을 숫자로 설정해서 switch 문을 이용해서 상황에 따른 에러메시지를 출력해줄 수 있겠다.
        * */

        if(returnString.equals("success"))
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                    appCompatActivity.finish();
                }
            });
        }
        else
        {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "회원가입이 실패 하였습니다", Toast.LENGTH_SHORT).show();
                    appCompatActivity.finish();
                }
            });
        }
    }

    public String request()
    {
        String returnString = null;
        //String response = "";

        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10000);      // 타임아웃 10초 설정
            conn.setUseCaches(false);           // 캐시 사용 안함
            conn.setRequestMethod("POST");      // Post로 연결
            conn.setDoInput(true);              // InputStream으로 서버로 부터 응답 헤더와 메시지를 읽어들이겠다는 옵션을 정의한다.
            conn.setDoOutput(true);             // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션을 정의한다.
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
           /*
              요청 헤더를 정의한다.( 원래 Content-Length값을 넘겨주어야하는데 넘겨주지 않아도 되는것이 이상하다. )
              무슨소리인지 모르겠다
           */

            OutputStream outputStream = conn.getOutputStream();
            outputStream.write((id + "||" + pwd + "||" + email).getBytes());
            outputStream.flush();           // 스트림의 버퍼를 비워준다.
            outputStream.close();           // 스트림을 닫는다.

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream())); // 서버의 응답을 읽기 위한 입력 스트림
            returnString = bufferedReader.readLine();

            /* 여러개의 응답을 받아와야 하는 경우 사용
            while ((returnString = bufferedReader.readLine()) != null) // 서버의 응답을 읽어옴
                response += returnString;
            */
            bufferedReader.close();

            conn.disconnect();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnString;
    }


}

/*http://acholyte.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%9B%B9-POST%EB%A1%9C-%EB%A7%A4%EA%B0%9C%EB%B3%80%EC%88%98-%EC%A0%84%EB%8B%AC
* 이건 뭐지
* */

/*
http://hangukin.tistory.com/339
허니콤 이상의 버전에서 HTTP 연결을 구현하고자 할 때에는 메인 액티비티에서 구현할 경우 StrictMode$AndroidBlockGuardPolicy.onNetwork 에러가 발생한다.
따라서 별로의 스레드를 구성하여 HTTP 연결을 구현하야 한다.
이 글에서는 허니콤 버전의 환경에서 HttpURLConnection 클래스를 사용하여 스레드를 통해 HTTP 연결을 하고 POST 메세지를 웹 서버로 전송하는 방법을 다루고자 한다.
디바이스 외의 장치와 서로 상호작용하는 HTTP 네트워킹의 경우에는 항상 예측하지 못한 딜레이를 야기할 수 있다.
따라서 메인 액티비티(혹은 UI 스레드) 외에 별로의 스레드에서 HTTP 동작을 수행하는 것이 올바른 구현이다.
AsyncTask 클래스는 메인 액티비티와 별도의 스레드를 구현하기 위한 간단한 방법 중 하나다.
AsyncTask는 짧은 기간 동안만 동작하는 스레드를 구현하는데 최적화되어 있다.
따라서 지금과 같이 잠시 POST를 하는 등의 동작에는 적합하지만 오랜 시간 동안 백그라운드에서 HTTP 연결을 유지하기에는 적합하지 않다.
 우선 AsyncTask를 상속받는 HttpConnectionThread 라는 클래스를 메인 액비티비 클래스의 내부 클래스로 정의한다.
 (메인 클래스의 내부 클래스로 정의해야 UI 요소에 접근하기가 용의하다.)
 AsyncTask 내의 메소드 중 doInBackground() 메소드는 백그라운드에서 HTTP 연결에 실제하는 부분이 동작되고,
 onPostExecute() 메소드는 HTTP 웹 서버로 부터 받아온 정보 등을 UI에 전달하기 위해 사용된다.
 */

/* 시발 이거 어떻게 써먹으라는거야 슈발
안드로이드/Android HttpURLConnection클래스로 POST 요청하기

// 요청할 파라미터의 정보를 입력한다.

String body = "id=asdf&pass=asdf";

// URL클래스의 생성자로 주소를 넘겨준다.

URL u = new URL( 주소 );

// 해당 주소의 페이지로 접속을 하고, 단일 HTTP 접속을 하기위해 캐스트한다.

HttpURLConnection  huc = (HttpURLConnection) u.openConnection();

// POST방식으로 요청한다.( 기본값은 GET )

huc.setRequestMethod("POST");

// InputStream으로 서버로 부터 응답 헤더와 메시지를 읽어들이겠다는 옵션을 정의한다.

huc.setDoInput(true);

// OutputStream으로 POST 데이터를 넘겨주겠다는 옵션을 정의한다.

huc.setDoOutput(true);

// 요청 헤더를 정의한다.( 원래 Content-Length값을 넘겨주어야하는데 넘겨주지 않아도 되는것이 이상하다. )

huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

// 새로운 OutputStream에 요청할 OutputStream을 넣는다.

OutputStream os = huc.getOutputStream();

// 그리고 write메소드로 메시지로 작성된 파라미터정보를 바이트단위로 "EUC-KR"로 인코딩해서 요청한다.

// 여기서 중요한 점은 "UTF-8"로 해도 되는데 한글일 경우는 "EUC-KR"로 인코딩해야만 한글이 제대로 전달된다.

os.write( body.getBytes("euc-kr") );

// 그리고 스트림의 버퍼를 비워준다.

os.flush();

// 스트림을 닫는다.

os.close();

// 응답받은 메시지의 길이만큼 버퍼를 생성하여 읽어들이고, "EUC-KR"로 디코딩해서 읽어들인다.

BufferedReader br = new BufferedReader( new OutputStreamReader( huc.getInputStream(), "EUC-KR" ), huc.getContentLength() );

String buf;

// 표준출력으로 한 라인씩 출력

while( ( buf = br.readLine() ) != null ) {

System.out.println( buf );

}

// 스트림을 닫는다.

br.close();
 */