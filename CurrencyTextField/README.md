# CurrencyTextField

## 특징

- 처음, 중간, 끝 어디서든 수정 가능
- 다양한 타입(`BigDecimal`, `String`, `Int`, `Long`) 지원
- Material 디자인 지원
- 콤마와 단위를 포함한 문자와, 금액 자체 콜백 지원
- 소수점 지원
- 최대 값, 최대 길이 제한 지원
- 자유로운 커스텀 가능

## 사용법

## BasicCurrencyTextField

기본

```kotlin
BasicCurrencyTextField(initialAmount = 1234567)
```

<img width="116" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/469b9fec-7920-443b-83d3-dfbc563f3836">

### maxValue - 최대 값 제한

```kotlin
BasicCurrencyTextField(
    initialAmount = 50000L,
    modifier = Modifier.fillMaxWidth(),
    maxValue = 10000,
)
```

<img width="363" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/1a40b14f-10fe-4d4d-b328-12ca6f205cbf">

### maxLength - 최대 길이 제한

```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567,
    modifier = Modifier.fillMaxWidth(),
    maxLength = 5,
)
```

<img width="361" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/c5afede4-68f9-455e-9f9d-714cb151d6c6">

### OverbalanceStrategy

<details>
<summary><code>maxValueStrategy.Amount</code></summary>

#### `maxValueStrategy`
  
**MaxValue** - maxValue로 설정한 값으로 표시 (기본 설정)

```kotlin
BasicCurrencyTextField(
    initialAmount = 50000L,
    modifier = Modifier.fillMaxWidth(),
    maxValue = 10000,
    maxValueStrategy = OverbalanceStrategy.Amount.MaxValue,
)
```

<img width="363" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/338c6a7e-7103-419e-a4e2-d549d442fb01">

**Default** - 기본 값(0)으로 표시

```kotlin
BasicCurrencyTextField(
    initialAmount = 50000L,
    modifier = Modifier.fillMaxWidth(),
    maxValue = 10000,
    maxValueStrategy = OverbalanceStrategy.Amount.Default,
)
```

<img width="360" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/4e21ebc5-9a63-4c87-a14e-833698e8f456">

**Ignore** - maxValue 보다 큰 값은 입력되지 않음 (초기 값은 maxValue 로 설정)

```kotlin
BasicCurrencyTextField(
    initialAmount = 50000L,
    modifier = Modifier.fillMaxWidth(),
    maxValue = 10000,
    maxValueStrategy = OverbalanceStrategy.Amount.Ignore,
)
```

<img width="363" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/c52f4025-2cb3-4eed-8edc-f84a3bbf1fff">

</details>

<details>
<summary><code>maxValueStrategy.Text</code></summary>
  
#### `maxLengthStrategy`
  
**DropLast** - maxLength 를 초과하는 문자 개수만큼 뒤에서 자름 (기본 설정)
 
```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567,
    modifier = Modifier.fillMaxWidth(),
    maxLength = 5,
    maxLengthStrategy = OverbalanceStrategy.Text.DropLast
)
```
 
<img width="361" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/c5afede4-68f9-455e-9f9d-714cb151d6c6">

**DropFirst** - maxLength 를 초과하는 문자 개수만큼 앞에서 자름

```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567,
    modifier = Modifier.fillMaxWidth(),
    maxLength = 5,
    maxLengthStrategy = OverbalanceStrategy.Text.DropFirst
)
```
  
<img width="362" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/5483912d-d6e6-48a2-8368-1df74a931147">
  
**Default** - 기본 값(0)으로 표시
  
```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567,
    modifier = Modifier.fillMaxWidth(),
    maxLength = 5,
    maxLengthStrategy = OverbalanceStrategy.Text.Default
)
```
  
<img width="364" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/bab88af5-21b5-4566-9a5b-af2c7d402575">

**Ignore** - 최대 길이 도달 시 입력 무시 (초기 값은 DropLast로 maxLength 만큼 잘림)

```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567,
    modifier = Modifier.fillMaxWidth(),
    maxLength = 5,
    maxLengthStrategy = OverbalanceStrategy.Text.Ignore
)
```

<img width="359" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/0c958802-223d-49be-9026-b0ccd749a53f">

</details>

### 다양한 타입 지원

**`BigDecimal`**

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
)
```

**`String`**

```kotlin
BasicCurrencyTextField(
    initialAmount = "1234567",
    modifier = Modifier.fillMaxWidth(),
)
```

**`Long`**

```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567L,
    modifier = Modifier.fillMaxWidth(),
)
```

**`Int`**

```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567.toInt(),
    modifier = Modifier.fillMaxWidth(),
)
```

### 단위 숨기기

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    showSymbol = false,
)
```

<img width="361" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/ed24b93e-1db0-4dc9-a71f-207670788844">

### Modifier 사용

```kotlin
BasicCurrencyTextField(
    initialAmount = 1234567,
    modifier = Modifier
        .fillMaxWidth()
        .background(Color.Cyan)
        .padding(8.dp)
        .border(1.dp, Color.Red, RoundedCornerShape(8.dp))
        .padding(8.dp),
)
```

<img width="360" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/cce74a42-f999-46cc-b201-9345b9652448">

### 단위 변경

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    symbol = "원"
)
```

<img width="365" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/da7493ea-b337-422b-90b2-646caa36a9de">

### 단위 위치 변경 (앞, 뒤)

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    rearSymbol = false,
)
```

<img width="364" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/c5b91505-5664-4a7f-9f78-6901c6c88dd7">

### 텍스트 스타일 변경

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    textStyle = TextStyle.Default.copy(
        color = Color.Red,
        letterSpacing = TextUnit(8f, TextUnitType.Sp),
        textDecoration = TextDecoration.LineThrough,
    ),
)
```

<img width="363" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/deb2fada-9ed2-472e-b29c-1054e4b77503">

### 수정 가능 여부

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    editable = false,
)
```

`editable` 이 `false`이면 선택만 가능

<img width="369" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/14bfdd01-ff5f-4916-90a8-2f8c5ca9c4f2">

### 사용 가능 여부

```kotlin
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    enabled = false,
)
```

`enabled` 가 `false` 이면 수정 및 선택 불가

<img width="363" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/13a52b22-0bfa-499f-bc95-59866b7339f8">

### 인터렉션 설정

```kotlin
val mutableInteractionSource = MutableInteractionSource()
val isFocused = mutableInteractionSource.collectIsFocusedAsState()
BasicCurrencyTextField(
    initialAmount = BigDecimal("1234567"),
    modifier = Modifier.fillMaxWidth(),
    textStyle = androidx.compose.ui.text.TextStyle(color = if (isFocused.value) Color.Blue else Color.Black),
    interactionSource = mutableInteractionSource,
)
```

![화면 기록 2023-05-22 오후 11 05 46](https://github.com/mangbaam/CustomView/assets/44221447/68539596-7642-429c-8a1a-6f7a0e444f03)
