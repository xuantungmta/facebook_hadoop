## Hướng dẫn cài đặt 
1. cấp quyền cho file gradlew bằng lệnh `chmod +x gradlew`
2. chạy lệnh `./gradlew build`, gradle sẽ tải dependencies cho lần đầu tiên build
3. sau khi chạy xong vào thư mục `app/build/target/libs` sẽ thấy file `app.jar`, đây là file thực thi đã build xong
4. chạy bằng hadoop `hadoop jar app.jar facebook_hadoop.App /data/document/filtered_output_utf8.csv /data/document/output` trong filtered_output_utf8.csv là file đầu vào theo mẫu đã gửi
