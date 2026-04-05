# Tai lieu Functional Interface va Lambda

Tai lieu nay tong hop cac vi tri trong project dang dung Functional Interface va lambda de ban doc nhanh, kem goi y de trinh bay theo huong SOLID + functional programming.

## 1) Cac vi tri dang dung Functional Interface

### a) `src/main/java/com/example/house/view/staff/content/DefaultStaffContentFactory.java`
- `Map<StaffFeature, Function<String, Node>> strategies`
- Dang dung lambda/method reference:
  - `context -> homePanel.getRoot()`
  - `panels::contractPanel`
- Y nghia: ap dung huong Strategy thong qua `Function<String, Node>`.

### b) `src/main/java/com/example/house/repository/impl/JpaRepositorySupport.java`
- `withEntityManager(Function<EntityManager, T> callback)`
- `inTransaction(Function<EntityManager, T> callback)`
- Y nghia: truyen hanh vi vao ham de tai su dung logic mo/ dong EntityManager va transaction.

### c) `src/main/java/com/example/house/repository/impl/JpaContractRepository.java`
- Su dung callback lambda:
  - `inTransaction(em -> { ... })`
  - `withEntityManager(em -> ... )`
- Y nghia: repository gon, tap trung vao query thay vi lap lai boilerplate transaction.

### d) `src/main/java/com/example/house/view/staff/home/StaffHomePanel.java`
- `Consumer<String> onCreateContract`
- Lambda JavaFX:
  - `btn.setOnAction(e -> selectFloor(floor))`
  - `selectedProperty().addListener((obs, oldV, selected) -> ...)`
- Y nghia: truyen callback xu ly tao hop dong ma khong phu thuoc truc tiep vao man hinh khac.

### e) `src/main/java/com/example/house/view/staff/feature/StaffFeaturePanels.java`
- Helper cot bang:
  - `column(String title, Function<T, String> extractor, double width)`
- Lambda trong xu ly UI:
  - `setOnAction(event -> ...)`
  - `textProperty().addListener((obs, oldValue, value) -> ...)`
  - `runSafe(msg, () -> { ... })` (`Runnable`)
- Y nghia: giam code lap, tach xu ly cot bang va xu ly su kien.

### f) `src/main/java/com/example/house/view/staff/StaffDashboardView.java`
- `Runnable onLogout`
- Lambda cho listener/event:
  - `selectedItemProperty().addListener(...)`
  - `setOnMouseEntered(...)`, `setOnMouseExited(...)`

### g) `src/main/java/com/example/house/view/auth/LoginView.java`
- `Runnable onLogout = () -> { ... }`
- Lambda cho dang nhap va toggle hien/ an mat khau.

## 2) Cac Functional Interface ban dang dung trong project

- `java.util.function.Function<T, R>`
- `java.util.function.Consumer<T>`
- `java.lang.Runnable`
- Cac interface su kien cua JavaFX (vi du `EventHandler<ActionEvent>`) thong qua lambda trong `setOnAction(...)`.

## 3) Goi y de trinh bay mon (nang cao hon)

Neu muon bao cao dep hon, ban co the them custom functional interface:

```java
@FunctionalInterface
public interface InputValidator<T> {
    String validate(T input); // tra ve null neu hop le, hoac message loi
}
```

Ung dung:
- Tao danh sach validator cho form hop dong.
- Chay bang stream/lambda truoc khi goi service.
- Tach rieng validate khoi UI handler de dung hon voi SRP (SOLID).

## 4) Ghi chu nhanh

- Hien tai project chua co interface nao danh dau bang `@FunctionalInterface`.
- Nhung project da dung functional style kha ro thong qua `Function`, `Consumer`, `Runnable`, listener lambda.

