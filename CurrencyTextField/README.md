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
BasicCurrencyTextField(
    initialAmount = initValue,
    modifier = Modifier.fillMaxWidth(),
)
```

<img width="364" alt="image" src="https://github.com/mangbaam/CustomView/assets/44221447/3a145ca1-40ce-4afd-b51a-562c61e878ed">

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
