package imgconvertt.compressimg.convrtformat.model

import android.graphics.Bitmap
import java.io.Serializable

class MyPdfModel(
    var pdfName: String,

    var pdfPath: String,
    var pdfSize: String,
    var resolution: String,
    var originalSize: String,
    var Originalresolution: String,
    var pdfImage: Bitmap?
) : Serializable {
}