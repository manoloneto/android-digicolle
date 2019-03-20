package co.hillstech.digicollection.ui.battleResult

import android.graphics.Color
import android.os.Bundle
import android.view.View
import co.hillstech.digicollection.R
import co.hillstech.digicollection.Session
import co.hillstech.digicollection.activities.bases.BaseActivity
import com.squareup.picasso.Picasso
import com.wooplr.spotlight.utils.SpotlightSequence
import kotlinx.android.synthetic.main.activity_battle_result.*
import kotlinx.android.synthetic.main.view_action_bar.*

class BattleResultActivity : BaseActivity(), BattleResultPresenter.View {

    private val presenter = BattleResultPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_result)

        presenter.view = this

        presenter.requestBattleResult()

        setupActionBar()
    }

    override fun showBattleResults() {
        with(presenter.getBattleResults()) {
            Picasso.get().load(buddy?.image)
                    .placeholder(R.drawable.placeholder)
                    .into(viewBuddyImage)

            Picasso.get().load(wild?.image)
                    .placeholder(R.drawable.placeholder)
                    .into(viewWildImage)

            viewBuddyName.text = buddy?.species
            viewResultExp.text = "+ ${exp}"
            viewResultCoins.text = "+ $ ${coins}"
            viewMessage.text = result?.toMessage()

            viewBattleResult.visibility = View.VISIBLE
        }
    }

    override fun updateViewWallet(newWallet: Int) {
        viewWallet.text = "$ ${newWallet}"
    }

    override fun showProgressRing() {
        progressRingCall(this)
    }

    override fun hideProgressRing() {
        progressRingDismiss()
    }

    override fun setupActionBar() {
        viewActivityTitle.text = getString(R.string.battle_result)

        viewBackArrow.setOnClickListener { onBackPressed() }

        Session.user?.crest?.color.let {
            setStatusBarColor(it)
            viewActionBar.setCardBackgroundColor(Color.parseColor(it))
        }
    }

    override fun showSpotlights() {
        SpotlightSequence.getInstance(this, Session.spotlightConfig)
                .addSpotlight(viewResultExp, "Exp.", "Sempre que você ganhar uma batalha, seu parceiro vai ganhar experiência. Acumule experiência para poder evoluir.", "tutorialExperience")
                .addSpotlight(viewResultCoins, "DigiCoins", "Além de experiência, você ganha mais alguns DigiCoins por enfrentar um Digimon Infectado.", "tutorialBattleDigiCoins")
                .startSequence()
    }
}
