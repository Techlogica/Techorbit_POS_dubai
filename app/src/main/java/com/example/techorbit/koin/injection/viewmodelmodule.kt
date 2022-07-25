package com.example.techorbit.koin.injection

import com.example.techorbit.ui.fragment.live.LiveViewmodel
import com.example.techorbit.viewmodel.*
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewmodelmodule= module {
    viewModel { MyViewmodel(get()) }
    viewModel { CountryViewmodel(get()) }
    viewModel { RechargePlanViewModel(get(),get())}
    viewModel { ScratchCardViewmodel(get()) }
    viewModel { HomeViewmodel(get() ,get()) }
    viewModel { LiveViewmodel(get(),get()) }
    viewModel { SearchViewmodel(get()) }
    viewModel { WalletReport(get()) }
    viewModel { TerminalSalesReports(get()) }
    viewModel { VendorViewmodel(get()) }
    viewModel { TransactionViewmodel(get()) }
    viewModel { PurchaseViewmodel(get()) }
    viewModel { NotificationViewmodel(get()) }
    viewModel { InventoryViemodel(get()) }
    viewModel { RecieptViewmodel(get()) }
    viewModel { CommissionViewmodel(get()) }


}