package mezic.grega.hows_gregamezic.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_season_episode_dialog.*
import mezic.grega.hows_gregamezic.R
import mezic.grega.hows_gregamezic.utils.Util

class SeasonEpisodeDialogFragment: DialogFragment() {

    companion object {
        fun newIntent(): SeasonEpisodeDialogFragment {
            return SeasonEpisodeDialogFragment()
        }

        var callbackListener: SeasonEpisodeDialogCallback? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_season_episode_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        numberPicker_season.minValue = Util.MIN_SEASON_VALUE
        numberPicker_season.maxValue = Util.MAX_SEASON_VALUE

        numberPicker_episode.minValue = Util.MIN_EPISODE_VALUE
        numberPicker_episode.maxValue = Util.MAX_EPISODE_VALUE

        tv_save.setOnClickListener {
            val season = numberPicker_season.value
            val episode = numberPicker_episode.value
            callbackListener?.onNumberSelected(season, episode)
            fragmentManager?.popBackStack()
        }

    }

    fun setListener(callback: SeasonEpisodeDialogCallback?) {
        callbackListener = callback
    }
}

interface SeasonEpisodeDialogCallback {
    fun onNumberSelected(season: Int, episode: Int)
}