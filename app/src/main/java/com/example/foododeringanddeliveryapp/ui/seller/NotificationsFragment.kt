package com.joytekmotion.yemilicious.ui.seller


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.joytekmotion.yemilicious.R
import com.joytekmotion.yemilicious.ui.LoginActivity
import com.joytekmotion.yemilicious.ui.SetupActivity
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.io.ByteArrayOutputStream


@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class NotificationsFragment : Fragment() {
    private val DEFAULT_IMAGE_URL = "https://picsum.photos/200"
    private val REQUEST_IMAGE_CAPTURE = 100
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private lateinit var db: FirebaseFirestore
    private lateinit var imageUri: Uri
    private lateinit var backhome: LinearLayout
    private lateinit var imageView: ImageView
    private lateinit var camera: ImageView

    private lateinit var etfullname: TextView
    private lateinit var eefullname: TextView
    private lateinit var eeemail: TextView
    private lateinit var etemail: TextView
    private lateinit var etphone: TextView
    private lateinit var btnsave: Button
    private lateinit var choosephoto: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_notifications, container, false)


        etfullname = view.findViewById(R.id.ffullname)
        eefullname = view.findViewById(R.id.ettvfullname)
        eeemail = view.findViewById(R.id.ettvemail)
        etemail = view.findViewById(R.id.eemail)
        etphone = view.findViewById(R.id.pphone)
        backhome = view.findViewById(R.id.backhome)
        camera = view.findViewById(R.id.camera)
        btnsave = view.findViewById(R.id.btnsave)
        choosephoto = view.findViewById(R.id.choosephoto)


        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val isLogin = sharedPref.getString("Email", "1")

        if (isLogin == "1") {
            var email = requireActivity().intent.getStringExtra("email")
            if (email != null) {
                setText(email)
                with(sharedPref.edit())
                {
                    putString("Email", email)
                    apply()
                }
            } else {
                var intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)

            }
        } else {
            setText(isLogin)
        }

        backhome.setOnClickListener {
            startActivity(Intent(activity, SetupActivity::class.java))
        }


        btnsave.setOnClickListener {



            val name = ettvfullname.text.toString().trim()

            if (name.isEmpty()) {
                ettvfullname.error = "name required"
                ettvfullname.requestFocus()
                return@setOnClickListener
            }


            progressbar.visibility = View.VISIBLE



        }
        return view

    }
    private fun setText(email: String?) {
        db = FirebaseFirestore.getInstance()
        if (email != null) {
            db.collection("USERS").document(email).get()
                .addOnSuccessListener { tasks ->
                    ffullname.text = tasks.get("Name").toString()
                    ettvfullname.text = tasks.get("Name").toString()
                    ettvemail.text = tasks.get("email").toString()
                    pphone.text = tasks.get("Phone").toString()
                    eemail.text = tasks.get("email").toString()

                }
        }

    }
}