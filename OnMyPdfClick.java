package imgconvertt.compressimg.convrtformat.interfaces;

import android.view.View;

import imgconvertt.compressimg.convrtformat.model.MyPdfModel;

public interface OnMyPdfClick {

    void onClick(MyPdfModel myPdfModel);

    void onShareClick(View view,MyPdfModel myPdfModel);
    void onDeleteClick(View view,MyPdfModel myPdfModel,int position);

    void onLongClick(MyPdfModel myPdfModel,boolean check);
}
