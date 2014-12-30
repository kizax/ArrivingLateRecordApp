ArrivingLateRecordApp
================

1.在內部儲存空間建立 student 資料夾<br>

2.在 student 資料夾裡面放入學生名條，並重新命名為 studentData.xls<br>
<br>
檔案格式:<br>
   學號  | 年班 | 座號 |  姓名<br>
  210001 | 301 | 1 | 王大明<br>
  
  ps.前三欄儲存格須設定為數值，最末欄設為文字<br>

3.現在開始可以打開app使用了!<br>
輸入學生的學號或年班號，找到學生<br>
再按下"+"按鈕。將學生加到遲到紀錄中<br>
<br>
小技巧:<br>
輸入6碼學號 210001 ，可依學號查到學生<br>
輸入5碼年班號 31001 ，可依年班號查到學生<br>遲到紀錄儲存至
 \* 或 . 可以代表任何數字或文字(不知道完整學號、年班號或姓名時可使用此技巧)<br>
<br>
Ex.<br>
輸入6碼學號 21000. 或 21000* ，可依學號查到 210000 ~ 210009 的學生<br>
輸入5碼年班號 310.. 或 310** ，可依年班號查到 310 整班學生<br>

![alt tag](https://raw.githubusercontent.com/kizax/ArrivingLateRecordApp/master/res/picture/SearchingStudentFragment.jpg) ![alt tag](https://raw.githubusercontent.com/kizax/ArrivingLateRecordApp/master/res/picture/SearchingStudentFragment_after%20Adding.jpg)
<br>
4. 在遲到紀錄中，可刪除學生的遲到紀錄，<br>
只要按下"X"按鈕，便可將學生的遲到紀錄消除<br>
<br>
![alt tag](https://raw.githubusercontent.com/kizax/ArrivingLateRecordApp/master/res/picture/ArrivingLateRecordFragment.jpg)
![alt tag](https://raw.githubusercontent.com/kizax/ArrivingLateRecordApp/master/res/picture/ConfirmDialog.jpg)<br>
<br>
5. 按下儲存按鈕，則會將遲到紀錄儲存至Arriving Late Record資料夾中<br>
![alt tag](https://raw.githubusercontent.com/kizax/ArrivingLateRecordApp/master/res/picture/ArrivingLateRecordFragment_afterDeleting.jpg) <br>
![alt tag](https://raw.githubusercontent.com/kizax/ArrivingLateRecordApp/master/res/picture/ArrivingLateRecordFile.jpg) <br>
<br>
