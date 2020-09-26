package com.example.acquatikaapp.data.util;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.acquatikaapp.data.dto.ExportDataDto;
import com.example.acquatikaapp.data.repository.SalesOrderRepository;
import com.example.acquatikaapp.ui.util.DateUtil;
import com.example.acquatikaapp.ui.util.ValueUtil;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataExporter {

    private Application mApplication;
    private SalesOrderRepository mSalesOrderRepository;

     public DataExporter(Application application) {
         this.mApplication = application;
         this.mSalesOrderRepository = new SalesOrderRepository(application);
     }

     public void runExport() {
         Toast.makeText(mApplication, "Exporting data...", Toast.LENGTH_SHORT).show();
         new ExportDataAsyncTask().execute();
     }

     //NOTE: field are in alphabetical order
     private String[] getCsvHeader(Class clazz) {
         List<String> headerLabelList = new ArrayList<>();
         for(Field field : clazz.getDeclaredFields()) {
             headerLabelList.add(field.getName());
         }

         return headerLabelList.toArray(new String[headerLabelList.size()]);
     }

     private class ExportDataAsyncTask extends AsyncTask<Void, Void, File> {

         @Override
         protected File doInBackground(Void... voids) {
             DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy--hh:mm-aa");
             String baseDir = mApplication.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
             String fileName = "ACQUATIKA--"+dateFormat.format(new Date())+".csv";
             String filePath = baseDir + File.separator + fileName;
             File file = new File(filePath);
             CSVWriter writer = null;

             Log.i("PATH",filePath);
             try {

                 if(file.exists()&&!file.isDirectory()) {
                     FileWriter mFileWriter = new FileWriter(filePath, true);
                     writer = new CSVWriter(mFileWriter);
                 }
                 else {
                     writer = new CSVWriter(new FileWriter(filePath));
                 }

                 List<String[]> csvData = new ArrayList<>();
                 csvData.add(getCsvHeader(ExportDataDto.class));
                 List<ExportDataDto> dataToExport = mSalesOrderRepository.getExportData();
                 for (ExportDataDto data : dataToExport) {
                     csvData.add(new String[] {
                             data.getCustomerName(),
                             String.valueOf(data.getDate()),
                             ValueUtil.convertPriceToDisplayValue(data.getDiscount()),
                             ValueUtil.getOrderTypeName(data.getOrderType()),
                             ValueUtil.convertPriceToDisplayValue(data.getPrice()),
                             data.getProductName(),
                             String.valueOf(data.getQuantity()),
                             String.valueOf(data.getSalesOrderId()),
                             ValueUtil.getStatusName(data.getStatus()),
                             ValueUtil.convertPriceToDisplayValue(data.getTotalPrice())
                     });
                 }

                 writer.writeAll(csvData);
                 writer.close();
             } catch (IOException e) {
                 e.printStackTrace();
                 return null;
             }

             return file;
         }

         @Override
         protected void onPostExecute(File file) {

             if(file == null) {
                 Toast.makeText(mApplication, "Failed to export data.", Toast.LENGTH_SHORT).show();
                 return;
             }

             Toast.makeText(mApplication, "Finished exporting data.", Toast.LENGTH_SHORT).show();
             Toast.makeText(mApplication, "PATH: "+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

//             Uri path = Uri.fromFile(file);
//             Intent intent = new Intent(Intent.ACTION_VIEW);
//             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//             intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//             intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//             intent.setDataAndType(path, "text/csv");
//             mApplication.startActivity(intent);
         }
     }
}
