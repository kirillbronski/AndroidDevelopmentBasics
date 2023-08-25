package com.kbcoding.androiddevelopmentbasics.fragments

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kbcoding.androiddevelopmentbasics.R
import com.kbcoding.androiddevelopmentbasics.contract.HasCustomTitle
import com.kbcoding.androiddevelopmentbasics.contract.navigator
import com.kbcoding.androiddevelopmentbasics.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment<FragmentAboutBinding>(), HasCustomTitle {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAboutBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setText()
        initializeListeners()
    }

    override fun getTitleRes(): Int = R.string.about

    private fun setText() {
        val context = requireContext()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            binding.tvVersionName.text = context.packageManager.getPackageInfo(
                context.packageName, PackageManager.PackageInfoFlags.of(0L)
            ).versionName
            binding.tvVersionCode.text = context.packageManager.getPackageInfo(
                context.packageName, PackageManager.PackageInfoFlags.of(0L)
            ).longVersionCode.toString()
        } else {
            binding.tvVersionName.text = context.packageManager.getPackageInfo(
                context.packageName, 0
            ).versionName
            binding.tvVersionCode.text = context.packageManager.getPackageInfo(
                context.packageName, 0
            ).versionCode.toString()
        }
    }

    private fun initializeListeners() {
        binding.btnOk.setOnClickListener {
            onOkPressed()
        }
    }

    private fun onOkPressed() {
        navigator().goBack()
    }

}