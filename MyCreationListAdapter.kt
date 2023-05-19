package imgconvertt.compressimg.convrtformat.adapters

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.shockwave.pdfium.PdfDocument
import com.shockwave.pdfium.PdfiumCore
import imgconvertt.compressimg.convrtformat.R
import imgconvertt.compressimg.convrtformat.databinding.RowlayoutMycreationlistBinding
import imgconvertt.compressimg.convrtformat.interfaces.OnMyPdfClick
import imgconvertt.compressimg.convrtformat.model.MyPdfModel
import java.io.File


class MyCreationListAdapter(
    var context: Context,
) : RecyclerView.Adapter<MyCreationListAdapter.MyPdfViewHolder>() {
    var selectionArray: SparseBooleanArray = SparseBooleanArray(0)
    var multiSelected: ArrayList<MyPdfModel> = ArrayList()
    var myPdfModelList: MutableList<MyPdfModel> = mutableListOf()
    var isMultiSelected = false
    lateinit var onMyPdfClick: OnMyPdfClick

    class MyPdfViewHolder(binding: RowlayoutMycreationlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var itemBinding = binding
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyPdfViewHolder {
        var binding: RowlayoutMycreationlistBinding =
            RowlayoutMycreationlistBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyPdfViewHolder(binding)
    }

    fun setListener(onMyPdfClick: OnMyPdfClick) {
        this.onMyPdfClick = onMyPdfClick
    }

    fun setData(list: List<MyPdfModel>) {
        myPdfModelList = list as MutableList<MyPdfModel>
        notifyDataSetChanged()
    }

    fun getMultiselectList(): List<MyPdfModel> {
        return multiSelected
    }

    override fun onBindViewHolder(holder: MyPdfViewHolder, position: Int) {

        var myPdfModel = myPdfModelList[position]

        //holder.itemBinding.tvPdfName.text = myPdfModel.pdfName
        holder.itemBinding.tvPdfSize.text = myPdfModel.pdfSize
        holder.itemBinding.tvPdfResolution.text = myPdfModel.resolution

        if (myPdfModel.pdfPath.endsWith(".pdf")) {

            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(25))

            Log.e("loadingtime", "onBindViewHolder: loading ")
            Glide.with(context).load(myPdfModel.pdfImage).apply(requestOptions).placeholder(R.drawable.bgmycreation)
                .into(holder.itemBinding.img)

            /* Glide.with(context).load(myBitmap).into(object :
                 CustomTarget<Drawable>() {
                 override fun onLoadCleared(placeholder: Drawable?) {

                 }

                 override fun onResourceReady(
                     resource: Drawable,
                     transition: Transition<in Drawable>?
                 ) {
                     holder.itemBinding.img.setImageDrawable(resource)
                     holder.itemBinding.img.setBackgroundColor(ContextCompat.getColor(context,R.color.borderColor))

                 }

             })

 */
        } else {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(25))

            // val myBitmap: Bitmap = BitmapFactory.decodeFile(myPdfModel.pdfPath)
            Glide.with(context).load(File(myPdfModel.pdfPath)).apply(requestOptions).into(holder.itemBinding.img)
        }


        //-------------
        holder.itemBinding.ivShare.visibility = if (!isMultiSelected) View.VISIBLE else View.GONE
        holder.itemBinding.ivCancel.visibility = if (!isMultiSelected) View.VISIBLE else View.GONE
        holder.itemBinding.ivCheckedUnChecked.visibility =
            if (isMultiSelected) View.VISIBLE else View.GONE
        holder.itemBinding.ivCheckedUnChecked.isSelected =
            if (selectionArray[position]) true else false
        holder.itemBinding.ivShare.setOnClickListener {
            onMyPdfClick.onShareClick(it, myPdfModel)
        }
        holder.itemBinding.ivCancel.setOnClickListener {
            onMyPdfClick.onDeleteClick(it, myPdfModel, position)
        }

        holder.itemBinding.rrvPdf.setOnClickListener(View.OnClickListener {
            if (isMultiSelected) {
                if (selectionArray[position]) {
                    multiSelected.remove(myPdfModel)
                    selectionArray.delete(position)
                    if (multiSelected.isEmpty()) {
                        isMultiSelected = false
                        notifyDataSetChanged()
                    } else {
                        notifyItemChanged(position)
                    }
                } else {
                    multiSelected.add(myPdfModel)
                    selectionArray.put(position, true)
                    notifyItemChanged(position)
                }
                onMyPdfClick.onLongClick(myPdfModel, isMultiSelected)
            } else {
                onMyPdfClick.onClick(myPdfModel)
            }
        })

        holder.itemBinding.rrvPdf.setOnLongClickListener(View.OnLongClickListener {
            if (!isMultiSelected) {
                isMultiSelected = true
                selectionArray.put(position, isMultiSelected)
                multiSelected.add(myPdfModel)
                notifyDataSetChanged()
                onMyPdfClick.onLongClick(myPdfModel, isMultiSelected)
            }
            true
        })
    }


    override fun getItemCount(): Int {
        return myPdfModelList.size
    }


    fun isMultiSelected1(): Boolean {
        return isMultiSelected
    }

    fun closeMulti() {
        selectionArray.clear()
        multiSelected.clear()
        isMultiSelected = false
        notifyDataSetChanged()
        onMyPdfClick.onLongClick(null, isMultiSelected)
    }

    fun clearAll() {
        selectionArray.clear()
        multiSelected.clear()
        notifyDataSetChanged()
        onMyPdfClick.onLongClick(null, isMultiSelected)
    }

    fun selectAll() {
        selectionArray.clear()
        multiSelected.clear()
        notifyDataSetChanged()
        multiSelected.addAll(myPdfModelList)
        for (i in myPdfModelList.indices) {
            selectionArray.put(i, true)
        }
        notifyDataSetChanged()
        onMyPdfClick.onLongClick(null, isMultiSelected)
    }

    fun generateImageFromPdf(pdfUri: Uri?): Bitmap? {

        var bitmap: Bitmap? = null

        val pageNumber = 0
        val pdfiumCore = PdfiumCore(context)
        try {
            val fd: ParcelFileDescriptor? =
                context.contentResolver.openFileDescriptor(pdfUri!!, "r")
            val pdfDocument: PdfDocument =
                pdfiumCore.newDocument(fd)
            pdfiumCore.openPage(pdfDocument, pageNumber)
            val width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber)
            val height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber)
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNumber, 0, 0, width, height)
            pdfiumCore.closeDocument(pdfDocument) // important!
        } catch (e: Exception) {
            //todo with exception
        }
        return bitmap
    }
}