## Hướng dẫn cài đặt
1. chạy lệnh `wget https://dlcdn.apache.org/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz && tar -xvf apache-maven-3.8.8-bin.tar.gz`
2. copy vào thư mục `sudo mv apache-maven-3.8.8 /opt`
3. thêm dòng sau vào cuối file `~/.bashrc`:
```
M2_HOME='/opt/apache-maven-3.8.8'
export PATH="$M2_HOME/bin:$PATH"
``` 
4. chạy lệnh `source ~/.bashrc`
5. test cài đặt mvn `mvn -version` thấy output hiện phiên bản mvn là oki
6. vào lại thư mục source facebook_hadoop chạy lệnh sau `mvn install:install-file -Dfile=libs/marmot.jar -DgroupId=vncorenlp -DartifactId=marmot -Dversion=1.0 -Dpackaging=jar` thấy không báo lỗi thì chuyển sang bước build
## Hướng dẫn build
0. trước khi build chỉnh lại đường dẫn của model tại file `corenlp/wordsegmenter/Vocubulary.java, corenlp/wordsegmenter/WordSegmenter.java, WordCount.java`, dùng đường dẫn tại dòng 18 và dòng 23 như sau:
```
    String pathStopwordFile = Utils.jarDir + "/models/vietnamese-stopwords-dash.txt";
    String vocabPath = Utils.jarDir + "/models/wordsegmenter/vi-vocab";
    String modelPath = facebook_hadoop.pipeline.Utils.jarDir + "/models/wordsegmenter/wordsegmenter.rdr";
```
1. cấp quyền cho file gradlew bằng lệnh `chmod +x gradlew`
2. chạy lệnh `./gradlew build`, gradle sẽ tải dependencies cho lần đầu tiên build
3. sau khi chạy xong vào thư mục `app/build/target/libs` sẽ thấy file `app.jar`, đây là file thực thi đã build xong
## Hướng dẫn chạy
1. Copy file thực thi vào máy cài hadoop
2. Copy thư mục models vào trong máy hadoop, để cùng vị trí với file app.jar trên.
3. chạy bằng hadoop `hadoop jar app.jar facebook_hadoop.WordCount /data/document/filtered_output_utf8.csv /data/document/output` trong filtered_output_utf8.csv là file đầu vào theo mẫu đã gửi để thống kê từ khóa
4. chạy bằng hadoop `hadoop jar app.jar facebook_hadoop.WordSort /data/document/output/part-r-00000 /data/document/output1` để sắp xếp từ khóa, trong đó file part-r-00000 là file kết quả của bước số 3
