# OpenImage 图片查看大图库
[![](https://jitpack.io/v/hirezy/OpenImageLib.svg)](https://jitpack.io/#hirezy/OpenImageLib)


## 属于你的侵入性低的大图查看器，高仿微信完美的过渡动画，支持自定义视频播放器，也可以自定义加载图片的内核，例如Glide，Picasso或其他的

### 建议使用Glide效果更好，另外建议开启原图缓存（有些版本是自动缓存原图的） diskCacheStrategy(DiskCacheStrategy.ALL或DiskCacheStrategy.DATA) 本库设置为ImageDiskMode.CONTAIN_ORIGINAL


## 效果演示
 
RecyclerView场景  | 聊天页面
 ------ | ------   
 <img src="https://github.com/hirezy/OpenImageLib/blob/master/screenshot/SVID_20220731_203152_1.gif" alt="show" width="320px" alt="show" />  | <img src="https://github.com/hirezy/OpenImageLib/blob/master/screenshot/SVID_20220731_203549_1.gif" alt="show" width="320px" alt="show" /> |
 
打开视频  | 朋友圈 
 ------ | ------   
 <img src="https://github.com/hirezy/OpenImageLib/blob/master/screenshot/SVID_20220731_203923_1.gif" alt="show" width="320px" alt="show" />  | <img src="https://github.com/hirezy/OpenImageLib/blob/master/screenshot/friends_demo.gif" alt="show" width="320px" alt="show" /> |


 

## 特色功能

1，支持自定义图片加载引擎

2，支持多种图片缓存模式

3，支持聊天界面的查看大图功能

4，支持微信聊天页面大图不在聊天页面时回到点击位置的效果

5，支持全部 ImageView.ScaleType 显示模式的图片打开大图效果，并且新增startCrop、endCrop、autoStartCenterCrop、autoEndCenterCrop四种显示模式

6，支持图片和视频混合数据

7，支持传入包含图片的 RecyclerView、ViewPager、ViewPager2、ListView、GridView 和 多个ImageView 的调用方式，傻瓜式调用，无需关心图片切换后该返回到哪个位置

8，支持大图和大图阅读模式

9，支持自定义大图切换效果（PageTransformer）

## 使用步骤

### 第一步、Jitpack 引入方法

#### 首先、在项目根目录下的build.gradle添加

```gradle
allprojects {
    repositories {
        ...
        maven { url "https://www.jitpack.io" }
    }
}
```

**你可以选择下面三种的其中一种，在module下的build.gradle添加。**

#### A、直接引入完整版（同时支持查看图片和视频）
implementation 'com.github.hirezy.OpenImageLib:OpenImageFullLib:v1.0.0'
```
#### B、引入只带有图片引擎的版本（只支持查看图片）
implementation 'com.github.hirezy.OpenImageLib:OpenImageGlideLib:v1.0.0'
```
```
//OpenImageLib 是基础库，没有引入图片引擎和视频播放器
implementation 'com.github.hirezy.OpenImageLib:OpenImageLib:v1.0.0'
```

### 第二步. 简单一步调用即可

**你可以选择下面两种图片数据的其中一种**

#### A、直接将数据转化为 String 的List

```java

List<String> dataList = new ArrayList<>();
for (ImageEntity data : datas) {
    dataList.add(data.getImageUrl());
}

//在点击时调用（以下以RecyclerView为例介绍）
OpenImage.with(activity)
        //点击ImageView所在的RecyclerView（也支持设置setClickViewPager2，setClickViewPager，setClickGridView，setClickListView，setClickImageView）
        .setClickRecyclerView(recyclerView,new SourceImageViewIdGet() {
           @Override
           public int getImageViewId(OpenImageUrl data, int position) {
               return R.id.iv_image;//点击的ImageView的Id
           }
       })
       //点击的ImageView的ScaleType类型（如果设置不对，打开的动画效果将是错误的）
       .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP,true)
       //RecyclerView的数据
       .setImageUrlList(dataList, MediaType.IMAGE)
       //点击的ImageView所在数据的位置
       .setClickPosition(position)
       //开始展示大图
       .show();
```

#### B、在您的数据实体类上实现OpenImageUrl接口

**PS:列表中展示的图片链接和展示大图时所用链接是不同时，这种方式可以有更好的过渡效果**

```java
public class ImageEntity implements OpenImageUrl {
    public String photoUrl;//图片大图
    public String smallPhotoUrl;//图片小图
    public String coverUrl;//视频封面大图
    public String smallCoverUrl;//视频封面小图
    public String videoUrl;//视频链接
    public int resouceType; //0图片1视频 

    public ImageEntity(String url) {
        this.url = url;
    }

    public ImageEntity() {
    }

    @Override
    public String getImageUrl() {
        return resouceType ==1:coverUrl:photoUrl;//大图链接（或视频的封面大图链接）
    }

    @Override
    public String getVideoUrl() {
        return videoUrl;//视频链接
    }

    @Override
    public String getCoverImageUrl() {//这个代表前边列表展示的图片
        return resouceType ==1:smallCoverUrl:smallPhotoUrl;//封面小图链接（或视频的封面小图链接）
    }

    @Override
    public MediaType getType() {
        return resouceType ==1:MediaType.VIDEO:MediaType.IMAGE;//数据是图片还是视频
    }
}

```

**然后调用显示**

```java

//在点击时调用（以下以RecyclerView为例介绍）
OpenImage.with(activity)
        //点击ImageView所在的RecyclerView（也支持设置setClickViewPager2，setClickViewPager，setClickGridView，setClickListView，setClickImageView）
        .setClickRecyclerView(recyclerView,new SourceImageViewIdGet() {
           @Override
           public int getImageViewId(OpenImageUrl data, int position) {
               return R.id.iv_image;//点击的ImageView的Id
           }
       })
       //点击的ImageView的ScaleType类型（如果设置不对，打开的动画效果将是错误的）
       .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP,true)
       //RecyclerView的数据
       .setImageUrlList(datas)
       //点击的ImageView所在数据的位置
       .setClickPosition(position)
       //开始展示大图
       .show();
```

**PS.完整调用示例**

```java

//在点击时调用（以下以RecyclerView为例介绍）
OpenImage.with(activity)
        //点击ImageView所在的RecyclerView（也支持设置setClickViewPager2，setClickViewPager，setClickGridView，setClickListView，setClickImageView）
        .setClickRecyclerView(recyclerView,new SourceImageViewIdGet() {
           @Override
           public int getImageViewId(OpenImageUrl data, int position) {
               return R.id.iv_image;//点击的ImageView的Id
           }
       })
       //点击的ImageView的ScaleType类型（如果设置不对，打开的动画效果将是错误的）
       .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP,true)
       //RecyclerView的数据
       .setImageUrlList(datas)
       //点击的ImageView所在数据的位置
       .setClickPosition(position)
       //可不设置,默认ImageDiskMode.CONTAIN_ORIGINAL，如果Glide不缓存原图，请设置其他参数
       .setImageDiskMode(ImageDiskMode.CONTAIN_ORIGINAL)
       //可不设置（setImageDiskMode设置为RESULT或NONE时必须设置）
       .setItemLoadHelper(new ItemLoadHelper() {
           @Override
           public void loadImage(Context context, OpenImageUrl openImageUrl, String imageUrl, ImageView imageView, int overrideWidth, int overrideHeight, OnLoadCoverImageListener onLoadCoverImageListener) {
                //如果使用的Glide缓存模式是ImageDiskMode.RESULT(只保存目标图片大小),必须在加载图片时传入大小，详看Demo
                Glide.with(imageView).load(imageUrl)
                    .override(overrideWidth, overrideHeight)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                onLoadCoverImageListener.onLoadImageFailed();
                                return false;
                                }
                        
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                onLoadCoverImageListener.onLoadImageSuccess();
                                return false;
                    }
                }).into(imageView);
           }
       })
       //可不设置（定制页面样式，详细可看Wiki文档）
       .setOpenImageStyle(R.style.DefaultPhotosTheme)
       //开始展示大图
       .show();
```
### 额外步骤

#### A、如果您引用的库是 OpenImageLib 您需要实现BigImageHelper接口并设置它，它是加载大图的关键（以下以Glide为例）
**（如果您使用的是OpenImageFullLib或OpenImageGlideLib则不需要这一步）**

```java
 public class MyApplication extends Application {
     @Override
     public void onCreate() {
         super.onCreate();
         //初始化大图加载器
         OpenImageConfig.getInstance().setBigImageHelper(new BigImageHelperImpl());
     }
 }
 
 public class BigImageHelperImpl implements BigImageHelper {
    @Override
    public void loadImage(Context context, String imageUrl, OnLoadBigImageListener onLoadBigImageListener) {
        RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_RGB_565);
        Glide.with(context)
                    .load(imageUrl).apply(requestOptions).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    onLoadBigImageListener.onLoadImageFailed();
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    onLoadBigImageListener.onLoadImageSuccess(resource);
                    return false;
                }
            }).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

    }

    @Override
    public void loadImage(Context context, String imageUrl, ImageView imageView) {
         RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .format(DecodeFormat.PREFER_RGB_565);
            Glide.with(context)
                    .load(imageUrl).apply(requestOptions).into(imageView);

    }

}
```

#### B、如果您引用的库是 OpenImageLib 或 OpenImageGlideLib 需要查看视频的功能，需要以下步骤
**（如果您使用的是OpenImageFullLib则不需要这一步）**

```java

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化视频创建类
        OpenImageConfig.getInstance().setVideoFragmentCreate(new VideoFragmentCreateImpl());
    }
}
public class VideoFragmentCreateImpl implements VideoFragmentCreate {
   @Override
   public BaseFragment createVideoFragment() {
       return new VideoPlayerFragment();
   }
}

public class VideoPlayerFragment extends BaseImageFragment<ENDownloadView> {

   private FragmentVideoBinding binding;
   private String playerKey;
   private boolean isLoadImageFinish;
   protected boolean isPlayed;

   @Override
   protected ImageView getSmallCoverImageView() {//返回小封面图
       return binding.videoPlayer.getSmallCoverImageView();
   }

   @Override
   protected ImageView getPhotoView() {//返回大封面图，必须在小封面图下边
       return binding.videoPlayer.getCoverImageView();
   }

   @Override
   protected ENDownloadView getLoadingView() {//返回loadingView
       return (ENDownloadView) binding.videoPlayer.getLoadingView();
   }

   @Override
   protected void hideLoading(ENDownloadView pbLoading) {//隐藏loading需要特殊处理的重写这个
       super.hideLoading(pbLoading);
       pbLoading.release();
       binding.videoPlayer.getStartButton().setVisibility(View.VISIBLE);
   }

   @Override
   protected void showLoading(ENDownloadView pbLoading) {//显示loading需要特殊处理的重写这个
       super.showLoading(pbLoading);
       pbLoading.start();
       binding.videoPlayer.getStartButton().setVisibility(View.GONE);
   }

   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);
       binding.videoPlayer.findViewById(R.id.back).setOnClickListener(v -> close());
       playerKey = binding.videoPlayer.getVideoKey();
       binding.videoPlayer.goneAllWidget();
       isPlayed = false;
   }
   @Override
   protected void onTouchClose(float scale) {//下拉关闭回调
       super.onTouchClose(scale);
       binding.videoPlayer.findViewById(R.id.surface_container).setVisibility(View.GONE);
       binding.videoPlayer.goneAllWidget();
   }

   @Override
   protected void onTouchScale(float scale) {//下拉时回调
       super.onTouchScale(scale);
       binding.videoPlayer.goneAllWidget();
       if (scale == 0){
           binding.videoPlayer.showAllWidget();
       }
   }

   @Override
   protected void loadImageFinish(boolean isLoadImageSuccess) {
       isLoadImageFinish = true;
       play();
   }

   private void play(){
       if (isTransitionEnd && isLoadImageFinish && !isPlayed){//这里可以不等封面图加载完就播放，这个是为了更好的效果
           if (getLifecycle().getCurrentState() == Lifecycle.State.RESUMED){
               toPlay4Resume();
           }else {
               getLifecycle().addObserver(new LifecycleEventObserver() {
                   @Override
                   public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                       if (event == Lifecycle.Event.ON_RESUME){
                           toPlay4Resume();
                           source.getLifecycle().removeObserver(this);
                       }
                   }
               });
           }

           isPlayed = true;
       }
   }

   protected void toPlay4Resume(){
       binding.videoPlayer.playUrl(openImageBean.getVideoUrl());
       binding.videoPlayer.startPlayLogic();
   }

   @Override
   protected void onTransitionEnd() {
       super.onTransitionEnd();
       play();
   }

   @Override
   public void onResume() {
       super.onResume();
       if (playerKey != null) {
           GSYVideoController.resumeByKey(playerKey);
       }
   }

   @Override
   public void onPause() {
       super.onPause();
       if (playerKey != null) {
           GSYVideoController.pauseByKey(playerKey);
       }
   }

   @Override
   public void onDestroyView() {
       super.onDestroyView();
       if (playerKey != null) {
           GSYVideoController.cancelByKeyAndDeleteKey(playerKey);
       }
   }

   @Override
   public View getExitImageView() {//退出页面时需要保证封面图可见
       binding.videoPlayer.getThumbImageViewLayout().setVisibility(View.VISIBLE);
       return super.getExitImageView();
   }
}

```

# 混淆
GSYVideoPlayer 的混淆规则：

```
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, java.lang.Boolean);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
```

Glide 的混淆规则：

```
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

```

## 版本限制
最低SDK版本：minSdkVersion >= 21
- [PhotoView](https://github.com/Baseflow/PhotoView)


