
# Welcome to TAPI Group Example!

# Application Requirements
**[Demo Video](https://user-images.githubusercontent.com/7322091/112107924-2ab05100-8be2-11eb-81ff-8660ff934a88.mp4)**

**1. Home : Display photo from Unsplash API with view status :**

![ Home Loading](/resource/home_loading.png?raw=true)   ![ Home Error](/resource/home_error.png?raw=true)   ![ Home Normal](/resource/home_normal.png?raw=true)
![ Home More](/resource/home_load_more.png?raw=true)   ![ Home More Error](/resource/home_load_more_error.png?raw=true)

**2. Detail : Display photo after user click item**

![Detail](/resource/detail.png?raw=true)

# Technical requirements

 - Use MVVM pattern
 - Use [View Binding](https://developer.android.com/topic/libraries/view-binding) and [Data Binding](https://developer.android.com/topic/libraries/data-binding)
 - Use [Jetpack](https://developer.android.com/topic/libraries/architecture): ViewModel, LiveData, ..
 - Use [Navigation component](https://developer.android.com/guide/navigation)
 - Use [RecycleView](https://developer.android.com/guide/topics/ui/layout/recyclerview) to display Grid View
 - Use [Retrofit](https://square.github.io/retrofit/) to query Unsplash API
 - Use [Coroutines](https://developer.android.com/kotlin/coroutines)
 - Use [ConstraintLayout](https://developer.android.com/training/constraint-layout) to design layout

**Advance : **

 - Use [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) to display photo
 - Use [Flow](https://kotlinlang.org/docs/flow.html)
 - Navigate between fragments using animations ([Guide](https://developer.android.com/guide/fragments/animate))
 - Zoom in/out ImageView in Detail Photo


# Unsplash API 

    https://api.unsplash.com/photos

| param | Description |
|--|--|
| client_id | 6fa91622109e859b1c40218a5dead99f7262cf4f698b1e2cb89dd18fc5824d15 |
| page | Page number to retrieve. (Optional; default: 1) |
| per_page | Number of items per page. (Optional; default: 10) |

**Response:**
```json
[
  {
    "id": "LBI7cgq3pbM",
    "description": "A man drinking a coffee.",
    "urls": {
      "raw": "https://images.unsplash.com/face-springmorning.jpg",
      "full": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg",
      "regular": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=1080&fit=max",
      "small": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=400&fit=max",
      "thumb": "https://images.unsplash.com/face-springmorning.jpg?q=75&fm=jpg&w=200&fit=max"
    },
  },
  // ... more photos
]
```
