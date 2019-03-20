package co.hillstech.digicollection.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.hillstech.digicollection.R
import co.hillstech.digicollection.Session
import com.squareup.picasso.Picasso
import com.wooplr.spotlight.SpotlightConfig
import com.wooplr.spotlight.SpotlightView
import com.wooplr.spotlight.utils.SpotlightListener
import com.wooplr.spotlight.utils.SpotlightSequence
import kotlinx.android.synthetic.main.activity_lobby.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    var isSpotlightShowed: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifySpotlights()
    }

    private fun verifySpotlights() {
        val preferences = activity!!.getSharedPreferences("DigiCollePref", AppCompatActivity.MODE_PRIVATE)
        isSpotlightShowed = preferences.getString("spotlights", null) != null
    }

    override fun onResume() {
        super.onResume()
        updateUserHome()
        showSpotlights()
    }

    private fun showSpotlights() {
        var crest: View = activity!!.findViewById(R.id.viewCrest)
        var wallet: View = activity!!.findViewById(R.id.viewWallet)

        var spotlights = SpotlightSequence.getInstance(activity, Session.spotlightConfig)
                .addSpotlight(crest, "Crest of ${Session.user!!.crest.virtue}", "Este é o seu brasão.\nEle representa uma virtude sua, e será muito importante para evoluir seus Digimons.", "tutorialCrest")
                .addSpotlight(wallet, "Carteira", "Aqui estão suas DigiCoins.\nVocê vai usar-las para comprar itens e melhorias na loja.", "tutorialWallet")
                .addSpotlight(viewPartnerImage, "Parceiro", "Este é o seu parceiro.\nÉ com ele que você fará as primeiras batalhas, e é ele que te ajudará no começo de sua jornada.", "tutorialPartner")

        Session.user?.digivice?.let {
            spotlights.addSpotlight(viewDigiviceImage, "Digivice", "Aqui está o seu Digivice.\nToque nele para ver mais informações.", "tutorialDigivice")

            val preferences = activity!!.getSharedPreferences("DigiCollePref", AppCompatActivity.MODE_PRIVATE)

            preferences.edit()
                    .putString("spotlights", "showed")
                    .commit()
        }

        spotlights.addSpotlight(viewExperience, "Exp.", "Esta é a experiência do seu parceiro. Batalhe para acumular mais experiência para poder evoluir.", "tutorialExpHome")
                .addSpotlight(viewLevel, "Nível", "Aqui você pode ver o nível que seu Digimon está, para passar de nível deve encher a barra de experiência primeiro.", "tutorialLevelHome")

        spotlights.startSequence()
    }

    private fun updateUserHome() {
        Session.user?.partner?.let {
            viewPartnerName.text = it.species
            viewExperience.text = "${it.experience} pts"
            viewLevel.text = it.getLevel()

            viewProgressBar.max = (it.type * 1000)
            viewProgressBar.progress = it.experience

            Picasso.get().load(it.image)
                    .noPlaceholder()
                    .into(viewPartnerImage)
        }

        Session.user?.digivice?.let {
            layoutDigivice.visibility = View.VISIBLE
            layoutDigiviceModel.text = it.model + " of " + Session.user!!.crest.virtue
            Picasso.get().load(it.image)
                    .noPlaceholder()
                    .into(viewDigiviceImage)

            var digiviceFragment = DigiviceFragment().apply {
                model = it.model
                cooldown = it.cooldown
                maxLevel = it.maxLevel
                resume = it.resume
                image = it.image
            }

            layoutDigivice.setOnClickListener {
                digiviceFragment.show(activity!!.supportFragmentManager, "DIGIVICE_FRAGMENT")
            }

            if(!isSpotlightShowed) {
                showDigiviceSpotlight()
            }
        }
    }

    private fun showDigiviceSpotlight() {
        SpotlightView.Builder(activity).setConfiguration(Session.spotlightConfig)
                .target(viewDigiviceImage)
                .headingTvText("Digivice")
                .subHeadingTvText("Aqui está o seu Digivice.\nToque nele para ver mais informações.")
                .usageId("tutorialDigivice") //UNIQUE ID
                .show()
    }
}
