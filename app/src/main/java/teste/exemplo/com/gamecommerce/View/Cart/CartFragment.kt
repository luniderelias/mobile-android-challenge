package teste.exemplo.com.gamecommerce.View.Cart

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.recyclerView
import teste.exemplo.com.gamecommerce.Presenter.Cart.CartPresenter
import teste.exemplo.com.gamecommerce.Presenter.Cart.ICartPresenter
import teste.exemplo.com.gamecommerce.R
import teste.exemplo.com.gamecommerce.View.Main.MainActivity
import teste.exemplo.com.gamecommerce.View.SuccessPurchase.SuccessPurchaseFragmentView

class CartFragment : Fragment(), ICartFragmentView,
    CartAdapter.DataChangedResponse {
    override fun onDataChange() {
        cartPresenter.getData()
        adapter.notifyDataSetChanged()
    }

    private lateinit var adapter: CartAdapter
    private lateinit var cartPresenter: ICartPresenter

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartPresenter = CartPresenter(this)
        cartPresenter.getData()
        configureRecyclerView()
        configureAdapter()
    }

    override fun configureRecyclerView(){
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(
            activity
        )
        recyclerView.layoutManager = layoutManager
    }

    override fun configureAdapter(){
        adapter = CartAdapter(activity as Context)
        adapter.dataChanged = this
        recyclerView.adapter = adapter
    }

    override fun setOnClickListeners(){
        edit_address.setOnClickListener {
            //TODO: Add Edit Address Screen
        }
        edit_payment.setOnClickListener {
            //TODO: Add Edit Payment Method Screen
        }
        finish_purchase.setOnClickListener {
            cartPresenter.finishPurchase()
        }
        keep_buying.setOnClickListener {
            (activity as MainActivity).supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun updateToolbar(){
        (activity as MainActivity).configureToolbar(getString(R.string.purchase_cart), true)
    }

    override fun updateTotalPrice(totalPrice: String) {
        price.text = totalPrice
    }

    override fun updateDeliveryTax(delivery_tax: String) {
        delivery_tax_value.text = delivery_tax
    }

    override fun updatePaymentData(){
        card_number.text = getString(R.string.example_card_number)
        card_flag.text = getString(R.string.example_card_flag)
    }

    override fun updateAddress() {
        address_cep.text = getString(R.string.example_addess_cep)
        address.text = getString(R.string.example_address_line)
        address_city_state_country.text = getString(R.string.example_city_state_country)
    }

    override fun goToSuccessPurchaseScreen(){
        (activity as MainActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_container, SuccessPurchaseFragmentView(), "SuccessPurchaseFragmentView")
            .addToBackStack("SuccessPurchaseFragmentView")
            .commit()
    }

    override fun showTryAgainSnackbar() {
        Snackbar.make(
            recyclerView,
            getString(R.string.failed_load_data),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.try_again)) { cartPresenter.finishPurchase() }
            .setActionTextColor(ContextCompat.getColor(activity as Context, R.color.colorBlue))
            .show()
    }

    override fun getToken(): String {
        return getString(R.string.token)
    }

    override fun showEmptyCartToast(){
        Toast.makeText(activity, R.string.empty_cart,Toast.LENGTH_SHORT).show()
    }
}