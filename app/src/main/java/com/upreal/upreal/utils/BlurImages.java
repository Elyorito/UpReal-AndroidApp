package com.upreal.upreal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import com.squareup.picasso.Transformation;


/**
 * Created by Elyo on 15/09/15.
 */
public class BlurImages implements Transformation {


        protected static final int UP_LIMIT = 25;
        protected static final int LOW_LIMIT = 1;
        protected final Context context;
        protected final int blurRadius;


        public BlurImages(Context context, int radius) {
            this.context = context;

            if(radius<LOW_LIMIT){
                this.blurRadius = LOW_LIMIT;
            }else if(radius>UP_LIMIT){
                this.blurRadius = UP_LIMIT;
            }else
                this.blurRadius = radius;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap sourceBitmap = source;

            Bitmap blurredBitmap;
            blurredBitmap = Bitmap.createBitmap(sourceBitmap);

            /*RenderScript renderScript = RenderScript.create(context);

            Allocation input = Allocation.createFromBitmap(renderScript,
                    sourceBitmap,
                    Allocation.MipmapControl.MIPMAP_FULL,
                    Allocation.USAGE_SCRIPT);



            Allocation output = Allocation.createTyped(renderScript, input.getType());

            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript,
                    Element.U8_4(renderScript));

            script.setInput(input);
            script.setRadius(blurRadius);

            script.forEach(output);
            output.copyTo(blurredBitmap);*/

            //source.recycle();
            return blurredBitmap;
        }

        @Override
        public String key() {
            return "blurred";
        }
}
