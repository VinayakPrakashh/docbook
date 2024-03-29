package com.example.docbook;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ProductViewHolder> {
    private Context context;

public  String value,specialization;
    private List<Product> productList;
    private List<Product> productListFiltered;
    int lastPosition=-1;

    public PatientAdapter(Context context, List<Product> productList) {


        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;

    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_patient, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        if(holder.getAdapterPosition()>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context,R.anim.slide_in);
            ((PatientAdapter.ProductViewHolder)holder).itemView.startAnimation(animation);
            Product product = productListFiltered.get(position);
            holder.textViewname.setText(product.getName());
            holder.textViewNumber.setText("No: "+ product.getNumber());
            holder.textViewward.setText("Ward No: "+product.getWard());
            lastPosition=holder.getAdapterPosition();
        }
        Product product = productListFiltered.get(position);
        holder.textViewname.setText(product.getName());
        holder.textViewNumber.setText("No: "+ product.getNumber());
        holder.textViewward.setText("Ward No: "+product.getWard());
        specialization=product.getSpecialization();
        value=product.getName();

        StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("patients").child(specialization).child(value);

// Download the contents of the file as a byte array
        imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {

            @Override
            public void onSuccess(byte[] bytes) {

                // Create a bitmap image from the byte array
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Display the bitmap image in an ImageView

                holder.imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors that occur
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            value=product.getName();

                showBottomDialog();

            }
        });


    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewname,textViewNumber,textViewward;
ImageView imageView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.image_view);
            textViewname = itemView.findViewById(R.id.name_text_view);
            textViewNumber = itemView.findViewById(R.id.number_text_view);
            textViewward=itemView.findViewById(R.id.ward_text_view);

        }
    }

    public static class Product {
        private String name;
        private String number,ward,special;

        public Product(String name,String number,String special,String ward) {
            this.name = name;
            this.number = number;
            this.ward=ward;
            this.special=special;
        }

        public String getName() {
            return name;
        }


        public String getNumber() {
            return number;
        }
        public String getWard() {
            return ward;
        }
        public String getSpecialization() {
            return special;
        }
    }
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                if (searchText.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();

                    for (Product product : productList) {
                        if (product.getName().toLowerCase().contains(searchText)) {
                            filteredList.add(product);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
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
    DocumentReference parentDocRef = db.collection("patients").document(value);


parentDocRef.delete()
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
                                    Intent intent = new Intent(context, DoctorPatientsActivity.class);
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
    StorageReference storageRef = storage.getReference().child("patients/"+specialization+"/"+value);

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
