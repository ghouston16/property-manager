
package org.wit.property_manager.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.property_manager.R
import org.wit.property_manager.activities.UserActivity
import org.wit.property_manager.adapters.UserAdapter
import org.wit.property_manager.adapters.UserListener
import org.wit.property_manager.databinding.ActivityUserListBinding
import org.wit.property_manager.main.MainApp
import org.wit.property_manager.models.UserModel

class UserListActivity : AppCompatActivity(), UserListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityUserListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadUsers()

        registerRefreshCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, UserActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUserClick(user: UserModel) {
        val launcherIntent = Intent(this, UserActivity::class.java)
        launcherIntent.putExtra("user_edit", user)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadUsers() }
    }

    private fun loadUsers() {
        showUsers(app.users.findAll())
    }

    fun showUsers (users: List<UserModel>) {
        binding.recyclerView.adapter = UserAdapter(users, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}