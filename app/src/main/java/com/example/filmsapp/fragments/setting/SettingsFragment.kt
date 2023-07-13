package com.example.filmsapp.fragments.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.filmsapp.databinding.FragmentSettingsBinding
import com.example.filmsapp.fragments.ViewBindingFragment
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {

    private val vm: SettingVM by viewModel()

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSettingsBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.initialize(requireContext())

        combine(
            vm.themeSwitch,
            vm.systemThemeSwitch
        ) { values ->
            binding.themeSwitch.isChecked = values[0]
            binding.systemThemeSwitch.isChecked = values[1]
        }.launchIn(lifecycleScope)

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            vm.themeSwitchSetListener(isChecked)
        }

        binding.systemThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            vm.systemThemeSwitchSetListener(isChecked)
        }
    }
}