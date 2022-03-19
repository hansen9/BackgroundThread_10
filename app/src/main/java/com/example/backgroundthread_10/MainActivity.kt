package com.example.backgroundthread_10

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import java.lang.ref.WeakReference



class MainActivity : AppCompatActivity(), MyAsyncCallback {
    private lateinit var tv_status: TextView
    private lateinit var tv_desc: TextView


    companion object {
        private const val INPUT_STRING = "Halo Ini Demo AsyncTask!!"
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val demoAsync = DemoAsync(this)
        demoAsync.execute(INPUT_STRING)
    }
    override fun onPreExecute() {
        tv_status = findViewById(R.id.tv_status)
        tv_desc = findViewById(R.id.tv_desc)

        tv_status.setText(R.string.status_pre)
        tv_desc.text = INPUT_STRING
    }
    override fun onPostExecute(result: String) {
        tv_status = findViewById(R.id.tv_status)
        tv_desc = findViewById(R.id.tv_desc)
        
        tv_status.setText(R.string.status_post)
        tv_desc.text = result
    }

    private class DemoAsync(myListener: MyAsyncCallback): AsyncTask<String, Void, String>(){

        private val myListener: WeakReference<MyAsyncCallback>
        init {
            this.myListener = WeakReference(myListener)
        }

        companion object{
            private val LOG_ASYNC = "DemoAsync"
        }

        override fun onPreExecute(){
            super.onPreExecute()
            Log.d(LOG_ASYNC, "status : onPreExecute")
            val myListener = myListener.get()
            myListener?.onPreExecute()

        }

        override fun doInBackground(vararg params: String?): String {
            Log.d(LOG_ASYNC, "status : doInBackground")
            var output: String? = null

            try {
                val input = params[0]
                output = "$input Selamat Belajar!!"
                Thread.sleep(2000)
            } catch (e: Exception) {
                Log.d(LOG_ASYNC, e.message.toString())
            }
            return output.toString()
        }
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            Log.d(LOG_ASYNC, "status : onPostExecute")

            val myListener = this.myListener.get()
            myListener?.onPostExecute(result)
        }
    }

}
internal interface MyAsyncCallback {
    fun onPreExecute()
    fun onPostExecute(text: String)
}