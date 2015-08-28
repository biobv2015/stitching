package org.knime.knip.stitching.fusion;

import org.knime.knip.stitching.util.FusionType;

import net.imagej.ops.OpService;
import net.imglib2.Cursor;
import net.imglib2.FinalInterval;
import net.imglib2.RandomAccess;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.realtransform.AffineGet;
import net.imglib2.type.numeric.RealType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

public class Fusion {

    public static <T extends RealType<T>> RandomAccessibleInterval<T> fuse(
            String fusionType, RandomAccessibleInterval<T> in1,
            RandomAccessibleInterval<T> in2, AffineGet transform,
            OpService ops) {

        RandomAccessibleInterval<T> out = null;

        // FIXME
        int n = in1.numDimensions();
        long[] offset = new long[n];
        for (int i = 0; i < 2; i++) {
            offset[i] = (long) transform.get(i, 2);
        }
        // moving in1 such that in1 and in2 have the same point in their
        // origin
        RandomAccess<T> img1 = Views.extendZero(in2).randomAccess();
        RandomAccess<T> img2 =
                Views.translate(Views.extendZero(in2), offset).randomAccess();

        // output size is sum of img sizes minus the overlap
        long[] outImgsize = new long[in1.numDimensions()];
        for (int i = 0; i < in1.numDimensions(); i++) {
            outImgsize[i] = in1.dimension(i) + in2.dimension(i)
                    - (in1.dimension(i) - offset[i]); // for correct result
                                                      // size when 0
                                                      // indexing
        }

        FinalInterval outInterval =
                Intervals.createMinMax(0, 0, outImgsize[0], outImgsize[1]);

        if (fusionType == FusionType.AVERAGE) {
            // TODO verify
            out = calcAvg(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.INTENSITY_RANDOM_TILE) {
            // TODO
            out = calcIntensityRandomTile(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.LINEAR_BLENDING) {
            // TODO
            out = calcLinearBlending(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.MAX_INTENSITY) {
            // TODO verify
            out = calcMaxIntensity(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.MEDIAN) {
            // TODO
            out = calcMedian(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.MIN_INTENSITY) {
            // TODO verify
            out = calcMinIntensity(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.OVERLAY) {
            // TODO
            out = calcOverlay(ops, img1, img2, outInterval);
        } else if (fusionType == FusionType.NO_FUSE) {
            return null;
        }
        return out;
    }

    // FIXME Average, min and max result in ArrayIndexOutOfBoundsException
    private static <T extends RealType<T>> Img<T> calcAvg(OpService ops,
            RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {
        T type = (T) ops.create().nativeType(img1.get().getClass());
        type.setOne();
        type.add(type);
        Img<T> outImg = ops.create().img(outInterval, type);
        Cursor<T> outCursor = outImg.localizingCursor();
        long[] pos = new long[outImg.numDimensions()];

        while (outCursor.hasNext()) {
            outCursor.fwd();
            outCursor.localize(pos);
            img1.setPosition(pos);
            img2.setPosition(pos);

            T img1Value = img1.get();
            T img2Value = img2.get();
            img2Value.add(img1Value);
            img2Value.div(type);
            outCursor.get().set(img2Value);
        }
        return outImg;
    }

    private static <T extends RealType<T>> Img<T> calcIntensityRandomTile(
            OpService ops, RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {
        T type = (T) ops.create().nativeType(img1.get().getClass());
        Img<T> outImg = ops.create().img(outInterval, type);
        return outImg;
    }

    private static <T extends RealType<T>> Img<T> calcLinearBlending(
            OpService ops, RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {
        T type = (T) ops.create().nativeType(img1.get().getClass());
        Img<T> outImg = ops.create().img(outInterval, type);
        return outImg;
    }

    @SuppressWarnings("unchecked")
    private static <T extends RealType<T>> Img<T> calcMaxIntensity(
            OpService ops, RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {

        // TODO change with the others as well
        Img<T> outImg = ops.create().img(outInterval, img1.get());
        Cursor<T> outCursor = outImg.localizingCursor();
        long[] pos = new long[outImg.numDimensions()];
        while (outCursor.hasNext()) {
            outCursor.fwd();
            outCursor.localize(pos);
            img1.setPosition(pos);
            img2.setPosition(pos);

            T img1Value = img1.get();
            T img2Value = img2.get();
            if (img1Value.compareTo(img2Value) < 0) {
                outCursor.get().set(img2Value);
            } else {
                outCursor.get().set(img1Value);
            }
        }
        return outImg;
    }

    private static <T extends RealType<T>> Img<T> calcMinIntensity(
            OpService ops, RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {
        T type = (T) ops.create().nativeType(img1.get().getClass());
        Img<T> outImg = ops.create().img(outInterval, type);
        Cursor<T> outCursor = outImg.localizingCursor();
        long[] pos = new long[outImg.numDimensions()];
        while (outCursor.hasNext()) {
            outCursor.fwd();
            outCursor.localize(pos);
            img1.setPosition(pos);
            img2.setPosition(pos);

            T img1Value = img1.get();
            T img2Value = img2.get();
            if (img1Value.compareTo(img2Value) < 0) {
                outCursor.get().set(img1Value);
            } else {
                outCursor.get().set(img2Value);
            }
        }
        return outImg;
    }

    private static <T extends RealType<T>> Img<T> calcMedian(OpService ops,
            RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {
        T type = (T) ops.create().nativeType(img1.get().getClass());
        Img<T> outImg = ops.create().img(outInterval, type);
        return outImg;
    }

    private static <T extends RealType<T>> Img<T> calcOverlay(OpService ops,
            RandomAccess<T> img1, RandomAccess<T> img2,
            FinalInterval outInterval) {
        T type = (T) ops.create().nativeType(img1.get().getClass());
        Img<T> outImg = ops.create().img(outInterval, type);
        return outImg;
    }

}
