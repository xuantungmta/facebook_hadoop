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
1. cấp quyền cho file gradlew bằng lệnh `chmod +x gradlew`
2. chạy lệnh `./gradlew build`, gradle sẽ tải dependencies cho lần đầu tiên build
3. sau khi chạy xong vào thư mục `app/build/target/libs` sẽ thấy file `app.jar`, đây là file thực thi đã build xong
## Hướng dẫn chạy
1. Copy file thực thi vào máy cài hadoop
2. chạy bằng hadoop `hadoop jar app.jar facebook_hadoop.App /data/document/filtered_output_utf8.csv /data/document/output` trong filtered_output_utf8.csv là file đầu vào theo mẫu đã gửi
