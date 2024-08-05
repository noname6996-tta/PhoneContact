<h1 align="center">Ứng dụng quản lý danh bạ</h1>

## Chức năng
- Có chức năng nhập danh bạ từ 1 file có sẵn trong hệ thống : Có UI Loading, Success.
+ Có hai cách một là Import Small/ Medium/ Big Data sẽ lấy dữ liệu từ đường dẫn app/res/raw trong code
+ Lấy từ file trong bộ nhớ điện thoại
  
- Hiển thị danh sách danh bạ đã có.
+ Hiển thị thêm số lượng liên hệ hiện có trong danh bạ

- Chức năng thêm mới danh bạ : Chú ý không thêm danh bạ nếu đã tồn tại 1 bản ghi có trùng thông tin email & phone -> Báo lỗi.
+ Ấn icon "+" trên màn hình danh sách -> chuyển sang màn thêm liên lạc, nhập đầy đủ thông tin và ấn Done, nếu có lỗi sẽ hiển thị text đỏ phía dưới

- Chức năng xóa danh bạ.
+ Xóa một phần tử: Giữ lâu vào item muốn xóa -> Hệ thống hiển thị ô hỏi và chọn xóa
+ Xóa toàn bộ danh bạ: Chọn menu ấn Delete all Data

## Công nghệ sử dụng
- Lifecycle
- ViewModel
- ViewBinding
- Room
- Hilt Dagger
- Gson
- Kotlin Coroutines
- Architecture: MVVM Architecture (View - DataBinding - ViewModel - Model)
