package com.hirezy.openimage.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.hirezy.openimage.DataUtils;
import com.hirezy.openimage.bean.ImageEntity;
import com.hirezy.openimage.imageloader.MyImageLoader;
import com.flyjingfish.openimage.R;
import com.flyjingfish.openimage.databinding.ActivityGridviewBinding;
import com.hirezy.openimagelib.OpenImage;
import com.hirezy.openimagelib.beans.OpenImageUrl;
import com.hirezy.openimagelib.listener.ItemLoadHelper;
import com.hirezy.openimagelib.listener.OnLoadCoverImageListener;
import com.hirezy.openimagelib.listener.SourceImageViewIdGet;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static ExecutorService cThreadPool = Executors.newFixedThreadPool(5);;
    private ActivityGridviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setAllowEnterTransitionOverlap(true);
        binding = ActivityGridviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadData();
    }

    private void loadData() {
        cThreadPool.submit(() -> {
            List<ImageEntity> datas = new ArrayList<>();

            String response1 = DataUtils.getFromAssets(this, "listview_data.json");
            try {
                JSONArray jsonArray = new JSONArray(response1);
                for (int i = 0; i < jsonArray.length(); i++) {
                    ImageEntity itemData = new ImageEntity();
                    String url = jsonArray.getString(i);
                    itemData.url = url;
                    datas.add(itemData);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setData(datas);
        });
    }

    private void setData(List<ImageEntity> datas) {
        runOnUiThread(() -> binding.gridView.setAdapter(new MyAdapter(datas)));

    }

    protected class MyAdapter extends BaseAdapter{
        List<ImageEntity> datas;

        public MyAdapter(List<ImageEntity> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vHolder;
            if (convertView == null){
                convertView = LayoutInflater.from(GridViewActivity.this).inflate(R.layout.item_listview,parent,false);
                vHolder = new ViewHolder();
                vHolder.imageView = convertView.findViewById(R.id.iv_image);
                convertView.setTag(vHolder);
            } else {
                vHolder = (ViewHolder) convertView.getTag();
            }
            MyImageLoader.getInstance().load(vHolder.imageView,datas.get(position).getCoverImageUrl(),R.mipmap.img_load_placeholder,R.mipmap.img_load_placeholder);
            vHolder.imageView.setOnClickListener(v -> {
                OpenImage.with(GridViewActivity.this).setClickGridView(binding.gridView, new SourceImageViewIdGet<OpenImageUrl>() {
                            @Override
                            public int getImageViewId(OpenImageUrl data, int position) {
                                return R.id.iv_image;
                            }
                        })
                        .setAutoScrollScanPosition(true)
                        .setSrcImageViewScaleType(ImageView.ScaleType.CENTER_CROP,true)
                        .setImageUrlList(datas).setImageDiskMode(MyImageLoader.imageDiskMode)
                        .setItemLoadHelper(new ItemLoadHelper() {
                            @Override
                            public void loadImage(Context context, OpenImageUrl openImageUrl, String imageUrl, ImageView imageView, int overrideWidth, int overrideHeight, OnLoadCoverImageListener onLoadCoverImageListener) {

                                MyImageLoader.getInstance().load(imageView, imageUrl, overrideWidth, overrideHeight, R.mipmap.img_load_placeholder, R.mipmap.img_load_placeholder, new MyImageLoader.OnImageLoadListener() {
                                    @Override
                                    public void onSuccess() {
                                        onLoadCoverImageListener.onLoadImageSuccess();
                                    }

                                    @Override
                                    public void onFailed() {
                                        onLoadCoverImageListener.onLoadImageFailed();
                                    }
                                });
                            }
                        })
                        .setOpenImageStyle(R.style.DefaultPhotosTheme)
                        .setClickPosition(position).show();
            });
            return convertView;
        }

        class ViewHolder {
            ImageView imageView;
        }
    }


}
