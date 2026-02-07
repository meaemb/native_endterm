package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.repository.Comment
import com.example.campuscompanion.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) : UserRepository {

    override fun currentUserId(): String? = auth.currentUser?.uid
    override fun currentUserEmail(): String? = auth.currentUser?.email

    override suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun toggleFavorite(eventId: String) {
        val uid = currentUserId() ?: throw IllegalStateException("Not loggepackage com.example.campuscompanion.data.repository\n" +
                "\n" +
                "import com.example.campuscompanion.domain.repository.Comment\n" +
                "import com.example.campuscompanion.domain.repository.UserRepository\n" +
                "import com.google.firebase.auth.FirebaseAuth\n" +
                "import com.google.firebase.database.*\n" +
                "import kotlinx.coroutines.channels.awaitClose\n" +
                "import kotlinx.coroutines.flow.Flow\n" +
                "import kotlinx.coroutines.flow.callbackFlow\n" +
                "import kotlinx.coroutines.tasks.await\n" +
                "import javax.inject.Inject\n" +
                "\n" +
                "class UserRepositoryImpl @Inject constructor(\n" +
                "    private val auth: FirebaseAuth,\n" +
                "    private val db: FirebaseDatabase\n" +
                ") : UserRepository {\n" +
                "\n" +
                "    override fun currentUserId(): String? = auth.currentUser?.uid\n" +
                "    override fun currentUserEmail(): String? = auth.currentUser?.email\n" +
                "\n" +
                "    override suspend fun signIn(email: String, password: String) {\n" +
                "        auth.signInWithEmailAndPassword(email, password).await()\n" +
                "    }\n" +
                "\n" +
                "    override suspend fun signUp(email: String, password: String) {\n" +
                "        auth.createUserWithEmailAndPassword(email, password).await()\n" +
                "    }\n" +
                "\n" +
                "    override suspend fun signOut() {\n" +
                "        auth.signOut()\n" +
                "    }\n" +
                "\n" +
                "    override suspend fun toggleFavorite(eventId: String) {\n" +
                "        val uid = currentUserId() ?: throw IllegalStateException(\"Not logged in\")\n" +
                "        val ref = db.reference.child(\"users\").child(uid).child(\"favorites\").child(eventId)\n" +
                "        val snap = ref.get().await()\n" +
                "        if (snap.exists()) ref.removeValue().await() else ref.setValue(true).await()\n" +
                "    }\n" +
                "\n" +
                "    override suspend fun getFavoriteIds(): Set<String> {\n" +
                "        val uid = currentUserId() ?: return emptySet()\n" +
                "        val ref = db.reference.child(\"users\").child(uid).child(\"favorites\")\n" +
                "        val snap = ref.get().await()\n" +
                "        return snap.children.mapNotNull { it.key }.toSet()\n" +
                "    }\n" +
                "\n" +
                "    override fun observeComments(eventId: String): Flow<List<Comment>> = callbackFlow {\n" +
                "        val ref = db.reference.child(\"comments\").child(eventId)\n" +
                "\n" +
                "        val listener = object : ValueEventListener {\n" +
                "            override fun onDataChange(snapshot: DataSnapshot) {\n" +
                "                val list = snapshot.children.mapNotNull { child ->\n" +
                "                    val id = child.key ?: return@mapNotNull null\n" +
                "                    val map = child.value as? Map<*, *> ?: return@mapNotNull null\n" +
                "                    Comment(\n" +
                "                        id = id,\n" +
                "                        userId = map[\"userId\"] as? String ?: \"\",\n" +
                "                        userEmail = map[\"userEmail\"] as? String ?: \"\",\n" +
                "                        text = map[\"text\"] as? String ?: \"\",\n" +
                "                        timestamp = (map[\"timestamp\"] as? Number)?.toLong() ?: 0L\n" +
                "                    )\n" +
                "                }.sortedBy { it.timestamp }\n" +
                "                trySend(list).isSuccess\n" +
                "            }\n" +
                "\n" +
                "            override fun onCancelled(error: DatabaseError) {}\n" +
                "        }\n" +
                "\n" +
                "        ref.addValueEventListener(listener)\n" +
                "        awaitClose { ref.removeEventListener(listener) }\n" +
                "    }\n" +
                "\n" +
                "    override suspend fun addComment(eventId: String, text: String) {\n" +
                "        val uid = currentUserId() ?: throw IllegalStateException(\"Not logged in\")\n" +
                "        val email = currentUserEmail() ?: \"unknown\"\n" +
                "        val ref = db.reference.child(\"comments\").child(eventId).push()\n" +
                "        ref.setValue(\n" +
                "            mapOf(\n" +
                "                \"userId\" to uid,\n" +
                "                \"userEmail\" to email,\n" +
                "                \"text\" to text,\n" +
                "                \"timestamp\" to System.currentTimeMillis()\n" +
                "            )\n" +
                "        ).await()\n" +
                "    }\n" +
                "\n" +
                "    override suspend fun deleteComment(eventId: String, commentId: String) {\n" +
                "        val uid = currentUserId() ?: throw IllegalStateException(\"Not logged in\")\n" +
                "        val ref = db.reference.child(\"comments\").child(eventId).child(commentId)\n" +
                "        val snap = ref.get().await()\n" +
                "        val ownerId = (snap.value as? Map<*, *>)?.get(\"userId\") as? String\n" +
                "        if (ownerId == uid) ref.removeValue().await()\n" +
                "    }\n" +
                "}\nd in")

        val ref = db.reference.child("users").child(uid).child("favorites").child(eventId)
        val snap = ref.get().await()
        if (snap.exists()) ref.removeValue().await() else ref.setValue(true).await()
    }

    override suspend fun getFavoriteIds(): Set<String> {
        val uid = currentUserId() ?: return emptySet()
        val ref = db.reference.child("users").child(uid).child("favorites")
        val snap = ref.get().await()
        return snap.children.mapNotNull { it.key }.toSet()
    }

    override fun observeComments(eventId: String): Flow<List<Comment>> = callbackFlow {
        val ref = db.reference.child("comments").child(eventId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull { child ->
                    val id = child.key ?: return@mapNotNull null
                    val map = child.value as? Map<*, *> ?: return@mapNotNull null
                    Comment(
                        id = id,
                        userId = map["userId"] as? String ?: "",
                        userEmail = map["userEmail"] as? String ?: "",
                        text = map["text"] as? String ?: "",
                        timestamp = (map["timestamp"] as? Number)?.toLong() ?: 0L
                    )
                }.sortedBy { it.timestamp }
                trySend(list).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    override suspend fun addComment(eventId: String, text: String) {
        val uid = currentUserId() ?: throw IllegalStateException("Not logged in")
        val email = currentUserEmail() ?: "unknown"
        val ref = db.reference.child("comments").child(eventId).push()
        ref.setValue(
            mapOf(
                "userId" to uid,
                "userEmail" to email,
                "text" to text,
                "timestamp" to System.currentTimeMillis()
            )
        ).await()
    }

    override suspend fun deleteComment(eventId: String, commentId: String) {
        val uid = currentUserId() ?: throw IllegalStateException("Not logged in")
        val ref = db.reference.child("comments").child(eventId).child(commentId)
        val snap = ref.get().await()
        val ownerId = (snap.value as? Map<*, *>)?.get("userId") as? String
        if (ownerId == uid) ref.removeValue().await()
    }
}
