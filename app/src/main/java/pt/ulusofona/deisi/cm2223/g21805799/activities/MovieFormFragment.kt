package pt.ulusofona.deisi.cm2223.g21805799.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pt.ulusofona.deisi.cm2223.g21805799.R
import pt.ulusofona.deisi.cm2223.g21805799.data.MoviesRepository
import pt.ulusofona.deisi.cm2223.g21805799.databinding.FragmentMovieFormBinding
import pt.ulusofona.deisi.cm2223.g21805799.model.Coords
import pt.ulusofona.deisi.cm2223.g21805799.model.DataManager
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*


class MovieFormFragment : Fragment() {
    private lateinit var binding: FragmentMovieFormBinding
    private val model: DataManager = MoviesRepository.getInstance()
    lateinit var name: EditText
    lateinit var date: String
    lateinit var cinemaSpinner: Spinner
    lateinit var evaluation: EditText
    lateinit var submitButton: Button
    lateinit var dateButton: Button
    lateinit var builder: AlertDialog.Builder
    lateinit var observations: EditText


    var dateLong: Long = 0
    var cinemaString = ""
    val options = mutableListOf("Choose Cinema")
    val cinemaLocation = hashMapOf<String, Coords>()

    private val permissionId = 123
    private val REQUEST_CODE = 200



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        (activity as MainActivity).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        val view = inflater.inflate(R.layout.fragment_movie_form, container, false)
        binding = FragmentMovieFormBinding.bind(view)
        builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title)
        cinemaSpinner = binding.spinner
        model.getCinemas {
            it.map {
                options.add(it.name)
                cinemaLocation.put(it.name, Coords(it.latitude, it.longitude))
            }
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        cinemaSpinner.adapter = adapter


        cinemaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                cinemaString = options[position]
                Log.i("APP", cinemaString)
                // Do something with the selected option
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.i("APP", "Nothing selected in Spinner")

                // Handle case when nothing is selected
            }
        }
        return binding.root
    }

    private fun openGalleryForImages() {

        if (Build.VERSION.SDK_INT < 19) {
            var intent = Intent()
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose Pictures")
                , REQUEST_CODE
            )
        }
        else { // For latest versions API LEVEL 19+
            var intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE);
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){

            // if multiple images are selected
            if (data?.getClipData() != null) {
                var count = data.clipData?.itemCount

                for (i in 0..count!! - 1) {
                    var imageUri: Uri = data.clipData?.getItemAt(i)!!.uri
                    //     iv_image.setImageURI(imageUri) Here you can assign your Image URI to the ImageViews
                }

            } else if (data?.getData() != null) {
                // if single image is selected

                var imageUri: Uri = data.data!!
                //   iv_image.setImageURI(imageUri) Here you can assign the picked image uri to your imageview

            }
        }
    }

    override fun onStart() {
        super.onStart()

        binding.image.setOnClickListener { uploadImage() }
        binding.submitButton.setOnClickListener { onSubmit() }
        binding.dateButton.setOnClickListener { pickDate() }


        name = binding.name
        evaluation = binding.evaluation
        submitButton = binding.submitButton
        dateButton = binding.dateButton
        observations = binding.observations

    }

    private fun uploadImage() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // get image
            openGalleryForImages()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                permissionId
            )
        }
    }



    private fun isEmpty(text: EditText): Boolean {
        val str: CharSequence = text.text.toString()
        return TextUtils.isEmpty(str)
    }


    private fun pickDate() {
        // on below line we are adding
        // click listener for our button
        dateButton.setOnClickListener {
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our text view.
                    dateButton.text =
                        (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis();
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

    }

    private fun onSubmit() {
        var nameResult = ""
        var sucess = false
        if (isEmpty(name)) {
            name.error = getString(R.string.name_error)
        }
        if (isEmpty(evaluation) || evaluation.text.toString().equals("")) {
            evaluation.error = getString(R.string.evaluation_error)
        } else {
            if (parseInt(evaluation.text.toString()) !in 1..10) {
                evaluation.error = getString(R.string.evaluation_error)
            }
        }
        if (cinemaString == getString(R.string.cinema_text) || cinemaString == "") {
            cinemaSpinner.performClick()
        }
        if (dateButton.text.toString() == getString(R.string.date)) {
            pickDate()
        }
        if (!isEmpty(name) && !isEmpty(evaluation) && cinemaString != "" && parseInt(evaluation.text.toString()) in 1..10 &&
            dateButton.text.toString() != getString(R.string.date) && cinemaString != getString(R.string.cinema_text)) {

            val dateFormat = SimpleDateFormat("dd-MM-yyyy").parse(dateButton.text.toString())
            dateLong = dateFormat.time
            MoviesRepository.getInstance().getMovieName(name.text.toString()) { result ->
                if(result.isSuccess) {
                    Log.i("APP", "Name: $result")
                    nameResult = result.getOrNull().toString()
                    builder.setMessage(nameResult)
                        //builder.setMessage(R.string.dialog_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes) { _dialog, _id ->
                            Toast.makeText(activity, R.string.yes_choice,
                                Toast.LENGTH_SHORT).show()
                            MoviesRepository.getInstance().getMovie(name.text.toString(), cinemaString, cinemaLocation.get(cinemaString)!!.latitude, cinemaLocation.get(cinemaString)!!.longitude,parseInt(evaluation.text.toString()), dateLong, observations.text.toString(), true) { result ->
                                if(result.isSuccess) {
                                    sucess = true
                                    Log.i("APP", "Result: $result")
                                }
                            }
                            activity?.onBackPressed()
                        }
                        .setNegativeButton(R.string.no) { dialog, _id -> //  Action for 'NO' Button
                            dialog.cancel()
                            Toast.makeText(activity, R.string.no_choice,
                                Toast.LENGTH_SHORT).show()
                        }
                    CoroutineScope(Dispatchers.Main).launch {
                        val alert = builder.create()
                        alert.show()
                    }

                }
            }

        }
    }


}