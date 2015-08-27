package org.knime.knip.stitching;

import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.SCIFIOImgPlus;
import net.imagej.ImgPlus;
import net.imglib2.type.numeric.RealType;

import org.junit.Test;
import org.knime.knip.stitching.lib.PairwiseStitching;
import org.knime.knip.stitching.lib.StitchingParameters;
import org.knime.knip.stitching.util.AbstractOpTest;
import org.knime.knip.stitching.util.FusionType;

public class PairwiseStitchingTest<T extends RealType<T>> extends
        AbstractOpTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testAverage() throws ImgIOException {

        ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 =
                (SCIFIOImgPlus<T>) opener.openImgs(
                        "res/testimgs/img3-1.png.ome.tif").get(0);
        ImgPlus<T> sfimp2 =
                (SCIFIOImgPlus<T>) opener.openImgs(
                        "res/testimgs/img3-2.png.ome.tif").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp2);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.AVERAGE;

        PairwiseStitching.performPairWiseStitching(imp1, imp2, params, ops);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMin() throws ImgIOException {

        ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 =
                (SCIFIOImgPlus<T>) opener.openImgs(
                        "res/testimgs/img3-1.png.ome.tif").get(0);
        ImgPlus<T> sfimp2 =
                (SCIFIOImgPlus<T>) opener.openImgs(
                        "res/testimgs/img3-2.png.ome.tif").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp2);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.MIN_INTENSITY;

        PairwiseStitching.performPairWiseStitching(imp1, imp2, params, ops);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMax() throws ImgIOException {
        ImgOpener opener = new ImgOpener();

        ImgPlus<T> sfimp1 =
                (SCIFIOImgPlus<T>) opener.openImgs(
                        "res/testimgs/img3-1.png.ome.tif").get(0);
        ImgPlus<T> sfimp2 =
                (SCIFIOImgPlus<T>) opener.openImgs(
                        "res/testimgs/img3-2.png.ome.tif").get(0);

        ImgPlus<T> imp1 = ImgPlus.wrap(sfimp1);
        ImgPlus<T> imp2 = ImgPlus.wrap(sfimp2);

        StitchingParameters params = new StitchingParameters();
        params.fusionMethod = FusionType.MAX_INTENSITY;

        PairwiseStitching.performPairWiseStitching(imp1, imp2, params, ops);
    }

}
