
package com.reactlibraryimagelight;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.reactlibraryimagelight.ResponseHelper;
import com.facebook.react.bridge.ReadableMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import android.util.Base64;
import android.util.Log;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.os.AsyncTask;
import android.os.Build;
import android.graphics.ColorMatrixColorFilter;
import android.content.res.Resources;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;

import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;

public class RNImageLightModule extends ReactContextBaseJavaModule {

  public static final int INDEX_FRAME = 1;
  public static final int INDEX_LIGHT = 2;

  private final ReactApplicationContext reactContext;

  Bitmap filterBitmap = null;
  Bitmap sourceBitmap;
  int bitmapWidth;
  int bitmapHeight;

  ResponseHelper responseHelper;
  ReadableMap options;
  protected Callback callback;

  String IMAGE_SOURCE1 = "imageSource1", IMAGE_SOURCE2 = "imageSource2", DATA_TYPE1 = "dataType1",
      DATA_TYPE2 = "dataType2", OVERLAY_TYPE = "overlayType", IS_ACCSETS = "isAccsets";

  public RNImageLightModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNImageLight";
  }

  @ReactMethod
  public void getResourcesImageLight(final ReadableMap options, final Callback callback) {
    try {
      responseHelper = new ResponseHelper();
      this.callback = callback;
      this.options = options;

      Bitmap bitmap1 = null;
      Bitmap bitmap2 = null;

      //
      if (options.getBoolean(IS_ACCSETS)) {

        if (options.getString(IMAGE_SOURCE1) == null) {
          responseHelper.invokeError(callback, "Source1 Null");
          return;
        } else {
          if (options.getString(DATA_TYPE1).equals("Path")) {
            bitmap1 = BitmapFactory.decodeFile(options.getString(IMAGE_SOURCE1));
          } else {
            bitmap1 = encodeBitmapFromBase64(options.getString(IMAGE_SOURCE1));
          }
        }

        if (bitmap1 == null) {
          responseHelper.invokeError(callback, "Failed to decode. Path is incorrect or image is corrupted");
          return;
        } else {
          if (options.getInt(OVERLAY_TYPE) == -1) {
            responseHelper.putString("base64", decodeBipmapToBase64(bitmap1));
            responseHelper.putInt("width", bitmap1.getWidth());
            responseHelper.putInt("height", bitmap1.getHeight());
            responseHelper.invokeResponse(callback);
            return;
          } else {
            Bitmap[] btmArr = { bitmap1, null };
            // ----- Image Processing
            setBitmap(bitmap1);
            MyAsynctackOveray myAsynTaskOverlay = new MyAsynctackOveray();
            myAsynTaskOverlay.execute(btmArr);

            // ----- Image Processing
          }

        }
      } else {

        if (options.getString(IMAGE_SOURCE1) == null || options.getString(IMAGE_SOURCE2) == null) {
          responseHelper.invokeError(callback, "Source Null");
          return;
        } else {

          if (options.getString(DATA_TYPE1).equals("Path")) {
            bitmap1 = BitmapFactory.decodeFile(options.getString(IMAGE_SOURCE1));
          } else {
            bitmap1 = encodeBitmapFromBase64(options.getString(IMAGE_SOURCE1));
          }

          if (options.getString(DATA_TYPE2).equals("Path")) {
            bitmap2 = BitmapFactory.decodeFile(options.getString(IMAGE_SOURCE2));
          } else {
            bitmap2 = encodeBitmapFromBase64(options.getString(IMAGE_SOURCE2));
          }

        }

        if (bitmap1 == null || bitmap2 == null) {
          responseHelper.invokeError(callback, "Failed to decode. Path is incorrect or image is corrupted");
          return;
        } else {

          // ----- Image Processing
          Bitmap[] btmArr = { bitmap1, bitmap2 };
          setBitmap(bitmap1);
          MyAsynctackOveray myAsynTaskOverlay = new MyAsynctackOveray();
          myAsynTaskOverlay.execute(btmArr);

          // ----- Image Processing

        }

      }
    } catch (Exception ex) {
      Log.e("ERR", ex.getMessage());
    }

  }

  public void setBitmap(Bitmap btm) {
    sourceBitmap = btm;
    bitmapWidth = sourceBitmap.getWidth();
    bitmapHeight = sourceBitmap.getHeight();
    filterBitmap = null;
  }

  // ----------------------------------------------------------------------------------------------

  // AsyncTask Overlay
  public class MyAsynctackOveray extends AsyncTask<Bitmap, Void, Bitmap> {

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps) {

      Bitmap src1 = bitmaps[0];
      Bitmap src2 = bitmaps[1];

      filterBitmap = src1.copy(Bitmap.Config.ARGB_8888, true);

      Canvas cvs = new Canvas(filterBitmap);
      cvs.drawBitmap(src1, 0.0f, 0.0f, new Paint());
      if (true) {
        pipelineOverlay(filterBitmap, src2);
      } else {
        cancel(true);

      }

      return filterBitmap;

    }

    void pipelineOverlay(Bitmap btmPipe, Bitmap btmOverlay) {

      Bitmap overlayBitmap = getOverlayBitmap(btmOverlay);
      if (!(overlayBitmap == null || overlayBitmap.isRecycled())) {
        if (Build.VERSION.SDK_INT > 10) {
          if (options.getInt(OVERLAY_TYPE) == 1) {
            applyOverlay11(overlayBitmap, btmPipe, INDEX_LIGHT);
          } else {
            applyOverlay11(overlayBitmap, btmPipe, INDEX_FRAME);
          }
        }
      }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
      super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      super.onPostExecute(bitmap);

      String encodeIamge = "";
      if (bitmap != null) {
        encodeIamge = decodeBipmapToBase64(bitmap);
        responseHelper.putString("base64", encodeIamge);
        responseHelper.putInt("width", bitmap.getWidth());
        responseHelper.putInt("height", bitmap.getHeight());

        responseHelper.invokeResponse(callback);
      } else {
        responseHelper.invokeError(callback, "Failed! Bitmap Processed Null");
      }

      callback = null;
      options = null;

    }
  }

  // method Image processing

  Bitmap getOverlayBitmap(Bitmap btm) {

    Bitmap bitmap = null;

    Bitmap temp;

    if (btm == null) {
      bitmap = getBitmapType();
    } else {
      bitmap = btm;
    }

    if (bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
      temp = bitmap;
      bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
      if (bitmap != temp) {
        temp.recycle();
      }
    }
    int overlayWidth = bitmap.getWidth();
    int overlayHeight = bitmap.getHeight();
    if ((this.bitmapHeight > this.bitmapWidth && overlayHeight < overlayWidth)
        || (this.bitmapHeight < this.bitmapWidth && overlayHeight > overlayWidth)) {
      Matrix matrix = new Matrix();
      if (options.getBoolean(IS_ACCSETS)) {
        matrix.postRotate(90.0f);
      }
      temp = bitmap;
      bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), matrix, true);
      if (bitmap != temp) {
        temp.recycle();
      }
    }
    return bitmap;
  }

  Bitmap getBitmapType() {
    Resources res = reactContext.getResources();

    BitmapFactory.Options opts = new BitmapFactory.Options();
    opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

    opts.inSampleSize = INDEX_FRAME;

    switch (options.getInt(OVERLAY_TYPE)) {
    case 0:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_01, opts);
    case 1:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_02, opts);
    case 2:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_03, opts);
    case 3:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_04, opts);
    case 4:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_05, opts);
    case 5:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_06, opts);
    case 6:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_07, opts);
    case 7:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_08, opts);
    case 8:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_09, opts);
    case 9:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_10, opts);
    case 10:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_11, opts);
    case 11:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_12, opts);
    case 12:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_13, opts);
    case 13:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_14, opts);
    case 14:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_15, opts);
    case 15:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_16, opts);
    case 16:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_17, opts);
    case 17:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_18, opts);
    case 18:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_19, opts);
    case 19:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_20, opts);
    case 20:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_21, opts);
    case 21:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_22, opts);
    case 22:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_23, opts);
    case 23:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_24, opts);
    case 24:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_26, opts);
    case 25:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_27, opts);
    case 26:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_28, opts);
    default:
      return BitmapFactory.decodeResource(res, R.drawable.overlay_01, opts);
    }
  }

  public void applyOverlay11(Bitmap overlay, Bitmap btm, int screenMode) {
    Paint paint = new Paint(INDEX_FRAME);
    paint.setFilterBitmap(true);
    PorterDuff.Mode mode = PorterDuff.Mode.SCREEN;
    if (screenMode == 0) {
      mode = PorterDuff.Mode.OVERLAY;
    }
    PorterDuffXfermode porterMode = new PorterDuffXfermode(mode);
    if (screenMode == INDEX_LIGHT) {
      porterMode = null;
    }
    paint.setXfermode(porterMode);

    Matrix borderMatrix = new Matrix();

    float wScale = ((float) btm.getWidth()) / ((float) overlay.getWidth());
    float hScale = ((float) btm.getHeight()) / ((float) overlay.getHeight());
    borderMatrix.reset();
    borderMatrix.postScale(wScale, hScale);

    Canvas cvs = new Canvas(btm);
    cvs.drawBitmap(overlay, borderMatrix, paint);

  }

  static int isOverlayScreenMode(int index) {
    if (index == INDEX_LIGHT) {
      return INDEX_LIGHT;
    }
    return INDEX_FRAME;
  }

  // ----------------------------------------------------------------------------------------------

  //
  public Bitmap encodeBitmapFromBase64(String base64) {
    Bitmap btm = null;
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] imageBytes = baos.toByteArray();
      imageBytes = Base64.decode(base64, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    } catch (Exception ex) {
      Log.e("ERR1", ex.getMessage());
    }
    return btm;
  }

  public String decodeBipmapToBase64(Bitmap bitmap) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] b = baos.toByteArray();

    return Base64.encodeToString(b, Base64.DEFAULT);
  }

}