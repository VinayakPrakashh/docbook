package com.example.docbook;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdminPatientAdapter extends RecyclerView.Adapter<AdminPatientAdapter.ProductViewHolder> {
    private Context context;
public  String value;
    private List<Product> productList;

    public AdminPatientAdapter(Context context, List<Product> productList) {


        this.context = context;
        this.productList = productList;


    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);
        holder.textViewname.setText(product.getName());
        holder.textViewNumber.setText(product.getNumber());
        value=product.getName();
        Toast.makeText(context, "value", Toast.LENGTH_SHORT).show();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showBottomDialog();

            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname,textViewNumber;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.product_name);
            textViewNumber = itemView.findViewById(R.id.product_price);

        }
    }

    public static class Product {
        private String name;

        private String number;

        public Product(String name,String number) {
            this.name = name;
            this.number = number;
        }

        public String getName() {
            return name;
        }


        public String getNumber() {
            return number;
        }
    }
    public void  showBottomDialog(){
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.bottomsheet_layout);

        LinearLayout view=dialog.findViewById(R.id.view);
        LinearLayout edit=dialog.findViewById(R.id.edit);
        LinearLayout delete=dialog.findViewById(R.id.delete);
        LinearLayout add=dialog.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,AddPatientActivity.class);
                context.startActivity(intent);

            }
        });
       edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,EditPatientActivity.class);
                intent.putExtra("key", value);
                context.startActivity(intent);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
dialog.dismiss();

             delete();


            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(context,ViewPatientActivity.class);
                intent.putExtra("key", value);
                context.startActivity(intent);

            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
public void delete(){
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
    String specialization = sharedPreferences.getString("specialization", "").toString();
// Get a reference to the parent document
    DocumentReference parentDocRef = db.collection("doctor").document(specialization);

// Get a reference to the subcollection
    CollectionReference subcollectionRef = parentDocRef.collection("patient");

// Get a reference to the document to be deleted
    DocumentReference docRef = subcollectionRef.document(value);

// Delete the document
    docRef.delete()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Patient Data Cleared")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    // Start the new activity
                               deleteimage();
                                    Intent intent = new Intent(context, AdminPatientsActivity.class);
                                    context.startActivity(intent);
                                    // Dismiss the dialog
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .show();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    new SweetAlertDialog(context)
                            .setTitleText("Failed to Clear Data")
                            .show();
                }
            });

}

public void deleteimage(){
    SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);
    String specialization = sharedPreferences.getString("specialization", "").toString();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference().child("patients/"+specialization+"/"+value+"/image");

    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            // File deleted successfully
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Uh-oh, an error occurred!
        }
    });

}
}
