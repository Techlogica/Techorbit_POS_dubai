package com.example.techorbit.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.techorbit.R
import com.example.techorbit.databinding.FragmentGamesAndGiftCardsBinding
import org.koin.android.ext.android.bind


class GamesAndGiftCards : Fragment() {

    private lateinit var binding: FragmentGamesAndGiftCardsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGamesAndGiftCardsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.playstation.setOnClickListener {
            val bundle= bundleOf("value" to 5)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }

        binding.xbox.setOnClickListener {
            val bundle= bundleOf("value" to 7)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.itunes.setOnClickListener {
            val bundle = bundleOf("value" to 8)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment, bundle)
        }
        binding.pubg.setOnClickListener {
            val bundle = bundleOf("value" to 9)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment, bundle)
        }
        binding.googlePlay.setOnClickListener {
            val bundle = bundleOf("value" to 12)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment, bundle)
        }

        binding.freeFire.setOnClickListener {
            val bundle= bundleOf("value" to 14)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.xboxUs.setOnClickListener {
            val bundle= bundleOf("value" to 15)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }

        binding.playstationUs.setOnClickListener {
            val bundle= bundleOf("value" to 16)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)

        }
        binding.playstationPlusUae.setOnClickListener {
            val bundle= bundleOf("value" to 17)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.playstationPlusUs.setOnClickListener {
            val bundle= bundleOf("value" to 18)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }

        binding.itunesUs.setOnClickListener {
            val bundle= bundleOf("value" to 19)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }


        binding.googlePlayUs.setOnClickListener {
            val bundle= bundleOf("value" to 20)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.amazon.setOnClickListener {
            val bundle= bundleOf("value" to 21)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.amazonUs.setOnClickListener {
            val bundle= bundleOf("value" to 22)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.netflix.setOnClickListener {
            val bundle= bundleOf("value" to 23)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
        binding.netflixUs.setOnClickListener {
            val bundle= bundleOf("value" to 24)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }

        binding.spotifyUae.setOnClickListener {
            val bundle= bundleOf("value" to 25)
            it.findNavController().navigate(R.id.action_gamesAndGiftCards_to_evoucherFragment,bundle)
        }
    }

}