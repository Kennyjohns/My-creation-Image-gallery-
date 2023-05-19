package imgconvertt.compressimg.convrtformat.activities;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import imgconvertt.compressimg.convrtformat.BuildConfig;
import imgconvertt.compressimg.convrtformat.R;
import imgconvertt.compressimg.convrtformat.adapters.MyCreationListAdapter;
import imgconvertt.compressimg.convrtformat.databinding.ActivityMycreationBinding;
import imgconvertt.compressimg.convrtformat.enums.Convertor_Enum;

import imgconvertt.compressimg.convrtformat.interfaces.OnMyPdfClick;
import imgconvertt.compressimg.convrtformat.model.MyPdfModel;
import imgconvertt.compressimg.convrtformat.utils.BaseTask;
import imgconvertt.compressimg.convrtformat.utils.IntentUtils;
import imgconvertt.compressimg.convrtformat.utils.StorageUtils;
import imgconvertt.compressimg.convrtformat.utils.TaskRunner;
import imgconvertt.compressimg.convrtformat.utils.Utils;

import com.google.android.material.tabs.TabLayout;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyCreationnActivity extends AppCompatActivity implements OnMyPdfClick {
    ActivityMycreationBinding binding;
    int appstickerindex = 0;

    ArrayList<String> stringArrayList;

    List<MyPdfModel> categoryModelArrayList;
    StorageUtils storageUtilsNew;

    MyCreationListAdapter myCreationListAdapter;

    String selectedPosition;
    String selectedpositionFromPreview;

    int deletePos = 0;

    Convertor_Enum convertor_enum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMycreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        stringArrayList = new ArrayList<>();
        storageUtilsNew = new StorageUtils(this);
        //  categoryModelArrayList = new ArrayList<>();

        setSupportActionBar(binding.toolbar);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        // Adding elements to the ArrayList
        stringArrayList.add("jpg");
        stringArrayList.add("pdf");
        stringArrayList.add("png");
        stringArrayList.add("bmp");
        stringArrayList.add("jpeg");
        stringArrayList.add("gif");
        stringArrayList.add("webp");

        myCreationListAdapter = new MyCreationListAdapter(this);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        myCreationListAdapter.setListener(this);

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.festival_layout_animation_full_up);
        binding.rvAllPdfs.setLayoutAnimation(controller);
        binding.rvAllPdfs.setLayoutManager(linearLayoutManager);
        binding.rvAllPdfs.setAdapter(myCreationListAdapter);

        setupAdapter(stringArrayList.get(0), Convertor_Enum.CONVERTOR_JPG);

        selectedPosition = stringArrayList.get(0);
        for (int i = 0; i < stringArrayList.size(); i++) {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(stringArrayList.get(i)));
        }

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    setupAdapter(stringArrayList.get(0), Convertor_Enum.CONVERTOR_JPG);
                    selectedPosition = stringArrayList.get(0);
                } else if (tab.getPosition() == 1) {
                    setupAdapter(stringArrayList.get(1), Convertor_Enum.CONVERTOR_PDF);
                    selectedPosition = stringArrayList.get(1);
                } else if (tab.getPosition() == 2) {

                    setupAdapter(stringArrayList.get(2), Convertor_Enum.CONVERTOR_PNG);
                    selectedPosition = stringArrayList.get(2);

                } else if (tab.getPosition() == 3) {
                    setupAdapter(stringArrayList.get(3), Convertor_Enum.CONVERTOR_BMP);
                    selectedPosition = stringArrayList.get(3);

                } else if (tab.getPosition() == 4) {
                    setupAdapter(stringArrayList.get(4), Convertor_Enum.CONVERTOR_JPEG);
                    selectedPosition = stringArrayList.get(4);
                } else if (tab.getPosition() == 5) {
                    setupAdapter(stringArrayList.get(5), Convertor_Enum.CONVERTOR_GIF);
                    selectedPosition = stringArrayList.get(5);
                } else if (tab.getPosition() == 6) {
                    setupAdapter(stringArrayList.get(6), Convertor_Enum.CONVERTOR_WEBP);
                    selectedPosition = stringArrayList.get(6);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        clickListeners();

    }

    private void setupAdapter(String prefix, Convertor_Enum convertor_enum) {

        this.convertor_enum = convertor_enum;
        File file = storageUtilsNew.getCreateStorageDirectoryPath(getResources().getString(R.string.app_folder_name) + "/" + convertor_enum.toString());
        categoryModelArrayList = new ArrayList<>();
        TaskRunner runner = new TaskRunner();
        runner.executeAsync(new CustomAsyncTask(this, categoryModelArrayList, file, prefix));

    }

    private List getList(File file, String selectedPosition) {

        List categoryModelArrayListfile = new ArrayList();
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            // for (File mFile : listFiles) {
            for (int i = 0; i < listFiles.length; i++) {

                if (listFiles[i].getAbsolutePath().endsWith("." + selectedPosition)) {
                    //   Log.e(TAG, "onViewCreated:" + listFiles[i].getAbsolutePath());


                    if(selectedPosition.equals("pdf"))
                    {


                        categoryModelArrayListfile.add(
                                new MyPdfModel(
                                        listFiles[i].getName(),
                                        listFiles[i].getAbsolutePath(),
                                        calculateProperFileSize(listFiles[i].length()),
                                        getDropboxIMGSize(listFiles[i]),
                                        calculateProperFileSize(listFiles[i].length()),
                                        getDropboxIMGSize(listFiles[i]),
                                        generateImageFromPdf(listFiles[i])

                                )
                        );
                    }else
                    {
                        categoryModelArrayListfile.add(
                                new MyPdfModel(
                                        listFiles[i].getName(),
                                        listFiles[i].getAbsolutePath(),
                                        calculateProperFileSize(listFiles[i].length()),
                                        getDropboxIMGSize(listFiles[i]),
                                        calculateProperFileSize(listFiles[i].length()),
                                        getDropboxIMGSize(listFiles[i]),
                                        null

                                )
                        );
                    }


                }
            }
        }

        Utils.sortCreation(categoryModelArrayListfile);

        return categoryModelArrayListfile;

    }


    private void clickListeners() {

        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myCreationListAdapter.getMultiselectList().size() > 0) {
                    deleteClick(null);
                } else {
                    Toast.makeText(MyCreationnActivity.this, "Select at least one pdf", Toast.LENGTH_SHORT).show();
                }

            }

        });

        binding.ivSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (categoryModelArrayList.size() == myCreationListAdapter.getMultiselectList().size()) {
                    myCreationListAdapter.clearAll();

                } else {
                    Log.e("TAG", "onClick: else");
                    myCreationListAdapter.selectAll();
                }
            }
        });

    }

    private void toggleClick(List<MyPdfModel> categoryModelArrayList) {

        if (categoryModelArrayList != null && categoryModelArrayList.size() > 0) {
            binding.llMain.setVisibility(View.VISIBLE);
            binding.llEmpty.setVisibility(View.GONE);
        } else {
            binding.llMain.setVisibility(View.GONE);
            binding.llEmpty.setVisibility(View.VISIBLE);
        }
    }

    String[] fileSizeUnits = {"bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"};

    public String calculateProperFileSize(long noOfBytes) {
        String sizeToReturn = "";
        double bytes = noOfBytes;
        int index = 0;
        while (index < fileSizeUnits.length) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
            index++;
        }

        bytes = Math.round(bytes * 100.0) / 100.0;
        sizeToReturn = bytes + " " + fileSizeUnits[index];
        return sizeToReturn;
    }

    private String getDropboxIMGSize(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String finalResolution = "" + imageWidth + "x" + imageHeight;
        return finalResolution;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            selectedpositionFromPreview = getIntent().getStringExtra(IntentUtils.INTENTKEY_SELECTEDPOSITION);

            Log.e("onactivityresult", "onActivityResult: " + selectedPosition);

            if (selectedPosition.equals("jpg")) {
                setupAdapter(stringArrayList.get(0), Convertor_Enum.CONVERTOR_JPG);
                selectedPosition = stringArrayList.get(0);
            } else if (selectedPosition.equals("pdf")) {

                setupAdapter(stringArrayList.get(1), Convertor_Enum.CONVERTOR_PDF);
                selectedPosition = stringArrayList.get(1);

            } else if (selectedPosition.equals("png")) {
                setupAdapter(stringArrayList.get(2), Convertor_Enum.CONVERTOR_PNG);
                selectedPosition = stringArrayList.get(2);

            } else if (selectedPosition.equals("bmp")) {
                setupAdapter(stringArrayList.get(3), Convertor_Enum.CONVERTOR_BMP);
                selectedPosition = stringArrayList.get(3);

            } else if (selectedPosition.equals("jpeg")) {
                setupAdapter(stringArrayList.get(4), Convertor_Enum.CONVERTOR_JPEG);
                selectedPosition = stringArrayList.get(4);
            } else if (selectedPosition.equals("gif")) {
                setupAdapter(stringArrayList.get(5), Convertor_Enum.CONVERTOR_GIF);
                selectedPosition = stringArrayList.get(5);
            } else if (selectedPosition.equals("webp")) {
                setupAdapter(stringArrayList.get(6), Convertor_Enum.CONVERTOR_WEBP);
                selectedPosition = stringArrayList.get(6);
            }


            //------------------------------

        }
    }

    @Override
    public void onClick(MyPdfModel myPdfModel) {
// arraylistImage = ArrayList<ImageItemModel>();
       /* if (selectedPosition.equals("pdf")) {
            startActivityForResult(
                    new Intent(this, PreviewPdfActivity.class)
                            .putExtra(getResources().getString(R.string.save_path), myPdfModel.getPdfPath()),
                    100
            );
        } else*/
        {

            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra(getResources().getString(R.string.save_path), myPdfModel.getPdfPath());
            intent.putExtra(IntentUtils.INTENTKEY_PREVIEWCLICKFROMMYCREATION, true);
            intent.putExtra(getResources().getString(R.string.pdf_password), "");
            intent.putExtra(IntentUtils.INTENTKEY_SELECTEDPOSITION, selectedPosition);

            if (selectedPosition.equals("png")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_PNG);
            } else if (selectedPosition.equals("jpg")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_JPG);
            } else if (selectedPosition.equals("jpeg")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_JPEG);
            } else if (selectedPosition.equals("webp")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_WEBP);
            } else if (selectedPosition.equals("gif")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_GIF);
            } else if (selectedPosition.equals("bmp")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_BMP);
            } else if (selectedPosition.equals("pdf")) {
                intent.putExtra(IntentUtils.ENUMVALUE, Convertor_Enum.CONVERTOR_PDF);
            }

            startActivityForResult(intent, 100);
            // startActivity(intent);
            // finish();

        /*    startActivityForResult(
                    intent,
                    100
            );*/
        }

    }

    @Override
    public void onShareClick(View view, MyPdfModel myPdfModel) {
        File file = new File(myPdfModel.getPdfPath());
        Intent share = new Intent(Intent.ACTION_SEND);
        String extension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        share.setType(MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension));
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share Document..."));
    }

    @Override
    public void onDeleteClick(View view, MyPdfModel myPdfModel, int position) {

        deleteClick(myPdfModel);
        //  showPopupMenu(view, myPdfModel);
        this.deletePos = position;

    }

    private void deleteClick(MyPdfModel myPdfModel) {

        Dialog dialog = new Dialog(this, R.style.rateusDialogAnimation);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.getWindow().setDimAmount(0.7f);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_bottomsheet);

        TextView yesBtn = (TextView) dialog.findViewById(R.id.tvDelete);
        TextView noBtn = (TextView) dialog.findViewById(R.id.tvCancel);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myCreationListAdapter.isMultiSelected1()) {
                    binding.llMain.setVisibility(View.GONE);
                    binding.llEmpty.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.ivDelete.setVisibility(View.GONE);
                    binding.ivSelectAll.setVisibility(View.GONE);

                    for (int i = 0; i < myCreationListAdapter.getMultiselectList().size(); i++) {
                        if (new File(myCreationListAdapter.getMultiselectList().get(i).getPdfPath()).exists()) {
                            new File(myCreationListAdapter.getMultiselectList().get(i).getPdfPath()).delete();
                        }
                    }
                    categoryModelArrayList.clear();
                    setupAdapter(selectedPosition, convertor_enum);
                    toggleClick(categoryModelArrayList);
                    binding.progressBar.setVisibility(View.GONE);
                    myCreationListAdapter.closeMulti();
                } else {
                    File file = new File(myPdfModel.getPdfPath());

                    if (file.exists()) {
                        file.delete();
                        categoryModelArrayList.remove(deletePos);
                        myCreationListAdapter.notifyItemRemoved(deletePos);
                        myCreationListAdapter.notifyItemRangeChanged(deletePos, categoryModelArrayList.size());
                        myCreationListAdapter.notifyDataSetChanged();
                    }

                    toggleClick(categoryModelArrayList);
                }
                dialog.dismiss();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onLongClick(MyPdfModel myPdfModel, boolean check) {

        if (check) {
            binding.ivDelete.setVisibility(View.VISIBLE);
            binding.ivSelectAll.setVisibility(View.VISIBLE);
        } else {
            binding.ivDelete.setVisibility(View.GONE);
            binding.ivSelectAll.setVisibility(View.GONE);
        }
        if (categoryModelArrayList.size() == myCreationListAdapter.getMultiselectList().size()) {
            binding.ivSelectAll.setImageResource(R.drawable.ic_selected_all);
        } else {
            binding.ivSelectAll.setImageResource(R.drawable.ic_deselected_all);
        }

    }


    private class CustomAsyncTask extends BaseTask {

        Context c1;
        File file;
        List<MyPdfModel> tabCategoriesItemArrayList;
        String prefix;


        Dialog progress;

        public CustomAsyncTask(Context c1, List<MyPdfModel> responseCategoryArrayList, File file, String prefix) {
            this.c1 = c1;
            this.file = file;
            this.prefix = prefix;
            progress = new Dialog(c1);
            this.tabCategoriesItemArrayList = responseCategoryArrayList;

        }

        @Override
        public void setUiForLoading() {

            if (prefix.equals("pdf")) {
                progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progress.setContentView(R.layout.progressdialog);
                progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progress.setCancelable(false);
                progress.setCanceledOnTouchOutside(false);
                progress.show();

                binding.llEmpty.setVisibility(View.GONE);
            }
            //  binding.progressBar.setVisibility(View.VISIBLE);
            //show progress bar
        }

        @Override
        public List<MyPdfModel> call() {

            //todo
            Log.e("loadingtime", "call: " + "1");
            // here position 0 means - allstories, position 1 means scary category stories - change according to tab later
          /*  if(convertor_enum.equals(Convertor_Enum.CONVERTOR_PDF)) {
                tabCategoriesItemArrayList = getList(file, prefix);
            }else*/
            {
                tabCategoriesItemArrayList = getList(file, prefix);
            }
            return tabCategoriesItemArrayList;
        }

        @Override
        public void setDataAfterLoading(Object result) {
            super.setDataAfterLoading(result);

            Log.e("loadingtime", "call: " + "2");

            //   binding.progressBar.setVisibility(View.GONE);
            if (prefix.equals("pdf")) {
                if (progress.isShowing()) {
                    binding.llEmpty.setVisibility(View.VISIBLE);
                    progress.dismiss();
                }
            }
            List<MyPdfModel> al1 = (ArrayList) result;
            categoryModelArrayList.clear();
            categoryModelArrayList = al1;
            myCreationListAdapter.setData(al1);
            // myCreationListAdapter.notifyDataSetChanged();

            toggleClick(al1);


        }
    }

    public Bitmap generateImageFromPdf(File pdfUri) {

        Bitmap bitmap = null;

        Uri uri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                pdfUri
        );

        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(uri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            pdfiumCore.renderPageBitmap(pdfDocument, bitmap, pageNumber, 0, 0, width, height);
            pdfiumCore.closeDocument(pdfDocument); // important!
        } catch (Exception e) {
            // todo with exception
        }
        return bitmap;
    }
}
