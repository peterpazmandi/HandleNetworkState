package com.inspirecoding.handlenetworkstate

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import com.inspirecoding.handlenetworkstate.databinding.ActivityMainBinding
import com.inspirecoding.handlenetworkstate.utils.NetworkUtils
import com.inspirecoding.handlenetworkstate.utils.getColorRes
import com.inspirecoding.handlenetworkstate.utils.hide
import com.inspirecoding.handlenetworkstate.utils.show

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private val ANIMATION_DURATION = 1000.toLong()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        handleNetworkChanges()
    }



    private fun handleNetworkChanges()
    {
        // 1
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConncted ->
            if (!isConncted)
            {
                // 2
                binding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                binding.networkStatusLayout.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            }
            else
            {
                // 3
                binding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                binding.networkStatusLayout.setBackgroundColor(getColorRes(R.color.colorStatusConnected))
                doAnimation(binding.networkStatusLayout)

            }
        })
    }

    private fun doAnimation(linearLayout: LinearLayout)
    {
        linearLayout.apply {
            animate()
                    .alpha(1f)
                    .setStartDelay(ANIMATION_DURATION)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?)
                        {
                            hide()
                        }
                    })
        }
    }
}