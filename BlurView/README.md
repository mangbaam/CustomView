## BlurView

https://github.com/mangbaam/CustomView/assets/44221447/ec37f3b5-415d-4f14-9a5b-d046c7d4eafe

### 특징

- Blur 처리를 위한 View
- Radius(블러 수준) 조정 가능
- Corner radius 일괄 혹은 개별 조정 가능
- Downsample 배율(화질 저하) 조정 가능
- xml 에 간단히 BlurView 추가하여 사용 가능
- RenderScript 활용한 블러 처리
- 손쉬운 블러 알고리즘 변경 가능

### 사용법

#### xml

```xml
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:id="@+id/iv_image"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:contentDescription="@string/description_blur_target_image"
        android:scaleType="fitXY"
        android:src="@drawable/image_small"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.mangbaam.blurview.BlurView
        android:id="@+id/blur_view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:blurRadius="4dp"
        app:cornerRadius="16dp"
        app:downsampleFactor="20.0"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.3" />
</androidx.coordinatorlayout.widget.CoordinatorLayout>
```

#### 속성

- `blurRadius`: 블러 수준 (1 ~ 25)
- `cornerRadius`: 코너 곡률
- `leftTopRadius`: 좌상단 코너 곡률
- `rightTopRadius`: 우상단 코너 곡률
- `rightBottomRadius`: 우하단 코너 곡률
- `leftBottomRadius`: 좌하단 코너 곡률
- `downsampleFactor`: 화질 저하 정도 (1 이상)
  - 값이 높아질 수록 타일 현상 심해짐
  - 값이 높을 수록 블러 처리 비용 감소

#### 메서드

- `setBlurRadius(radius: Float)`: 블러 수준 설정
- `setDownsampleFactor(factor: Float)`: 화질 저하 정도 설정
- `setCornerRadius(radius: Float)`: 전체 코너 곡률 설정
- `fun setCornerRadius(leftTop: Float, rightTop: Float, rightBottom: Float, leftBottom: Float)`: 개별 코너 곡률 설정
