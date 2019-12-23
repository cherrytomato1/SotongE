<?

function substring_index($subject, $delim, $count){
    if($count < 0){
        return implode($delim, array_slice(explode($delim, $subject), $count));
    }else{
        return implode($delim, array_slice(explode($delim, $subject), 0, $count));
    }
}
      header('content-type: text/html; charset=utf-8');
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
      $connect=@mysql_connect( "localhost", "root", "gncn") or
          die( "SQL server에 연결할 수 없습니다.");

     mysql_query("SET NAMES UTF8");
   // 데이터베이스 선택
     mysql_select_db("sotonge",$connect);

   // 세션 시작
  //   session_start();

     $id = $_POST['u_id'];
  //   //$feedid = $_POST['param0'];
     $subn = $_POST['u_pw'];
     $scores = $_POST['param0'];
     $msg = $_POST['param1'];
     $date = $_POST['param2'];

//  $id = 323;
//  $feedid = $_POST['param0'];
//  $subn = '모바일 프로그래밍';
//  $scores = '1.2.3.4.5.';
//  $msg = '테스트입니다';


  for($scc = substr_count($scores, "."),$i=0;$scc>=0;$scc--,$i++)
  {
      $score[$i] =  substring_index($scores,'.',1);
      //echo $tt[$tc];
      $scores = substring_index($scores,'.',-$scc);
    //echo $test;
  }

     $subidA=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     $subid=$subidA['SUBID'];

     $result=mysql_fetch_array(mysql_query("INSERT INTO feed_list(USERID, SUBID,FEEDDATE,FEEDSTATE) VALUES('$id', '$subid','$date','1')"));

     $result=mysql_fetch_array(mysql_query("INSERT INTO feed_cont(CONTENTS,SCORE1,SCORE2,SCORE3,SCORE4,SCORE5) VALUES('$msg','$score[0]','$score[1]','$score[2]','$score[3]','$score[4]')"));

     mysql_query("UPDATE sub_user SET LASTFEED= '$date' WHERE SUBID = '$subid' and USERID = '$id'");

     //mysql_query("INSERT INTO feed_cont(SCORE1) VALUES('$score[0]')");

     //result에 query 문으로 성공 실패 반환(TRUE,FALSE)

//$subid=mysql_fetch_array(mysql_query("SELECT SUBID FROM sub_list WHERE SUBNAME='$subn'"));
     if(!$result)   //실패시(FALSE)
          die("mysql query error");
     else
          echo "insert success"

?>
