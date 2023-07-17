package com.example.filmsapp.fragments.setting

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.filmsapp.R
import com.example.filmsapp.databinding.DialogBottomSheetSettingsBinding
import com.example.filmsapp.databinding.FragmentSettingsBinding
import com.example.filmsapp.fragments.ViewBindingFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {

    private val vm: SettingVM by viewModel()
    private val bottomSheetBinding by lazy { DialogBottomSheetSettingsBinding.inflate(layoutInflater) }

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.initialize(requireContext())

        vm.systemThemeString
            .onEach {
                print(it)
                binding.textTheme.text = it
            }
            .launchIn(lifecycleScope)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        binding.textTheme.setOnClickListener {
            bottomSheetDialog.show()
        }
        bottomSheetBinding.materialButton1.setOnClickListener {
            vm.themeSetListener(requireContext(), 1)
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.materialButton2.setOnClickListener {
            vm.themeSetListener(requireContext(), 2)
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.materialButton3.setOnClickListener {
            vm.themeSetListener(requireContext(), 3)
            bottomSheetDialog.dismiss()
        }
    }

//    fun setLocale(lang: String, context: Context): Context {
//        val locale = Locale(lang)
//        Locale.setDefault(locale)
//
//        val config = Configuration(context.resources.configuration)
//        config.setLocale(locale)
//
//        return context.createConfigurationContext(config)
//    }

}
