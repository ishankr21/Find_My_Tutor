package com.example.findmytutor.dataRepo

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.findmytutor.dataClasses.*
import com.example.findmytutor.dataClasses.RatingInfo
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FirebaseRepo: FirebaseMessagingService() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var COLLECTION_STUDENT:String = "Students"
    private var COLLECTION_TUTOR:String = "Tutor"
    private var COLLECTION_TUTOR_STUDENTS = "studentRequests"
    private var COLLECTION_STUDENT_TUTORS = "requestsSent"
    private var COLLECTION_DOUBT="doubts"
    private var COLLECTION_SOLUTIONS="solutions"
    private var COLLECTION_CHATS="chats"
    private var COLLECTION_CHATS_MESSAGES="messages"
    private var COLLECTION_RATINGS="Ratings"
    private var COLLECTION_TRANSACTIONS="Transactions"
    private var mFirestore = FirebaseFirestore.getInstance()

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    fun firebaseSignInWithPhone(credential: PhoneAuthCredential): MutableLiveData<String?> {

        val authenticatedUserLiveData = MutableLiveData<String?>()

        mAuth.signInWithCredential(credential)
            .addOnSuccessListener {

                    authenticatedUserLiveData.value = mAuth.currentUser!!.uid
                    Log.d("ishan","Nothing here")

            }
            .addOnFailureListener {
                    authenticatedUserLiveData.value = null
                    Log.d("ishan error","${it.message}")

            }

        return authenticatedUserLiveData

    }

    fun createStudent(student: Student,userId:String): MutableLiveData<Boolean> {

        val mStudentCreatedSuccess= MutableLiveData<Boolean>()
        val firebaseToken = FirebaseMessaging.getInstance().token.result
        student.tokenId = firebaseToken
        student.fcmTokens.add(firebaseToken)

        mFirestore.collection(COLLECTION_STUDENT).document(userId).set(student)
            .addOnSuccessListener {
                mStudentCreatedSuccess.value=true
            }
            .addOnFailureListener {
                mStudentCreatedSuccess.value=false
            }





        return mStudentCreatedSuccess

    }

    fun createTutor(tutor: Tutor,userId: String): MutableLiveData<Boolean> {
        val mTutorCreatedSuccess= MutableLiveData<Boolean>()
        val firebaseToken = FirebaseMessaging.getInstance().token.result
        tutor.tokenId = firebaseToken
        tutor.fcmTokens.add(firebaseToken)
        mFirestore.collection(COLLECTION_TUTOR).document(userId).set(tutor)
            .addOnSuccessListener {
                mTutorCreatedSuccess.value=true
            }
            .addOnFailureListener {
                mTutorCreatedSuccess.value=false
            }





        return mTutorCreatedSuccess
    }

    fun checkUserExists(phone:String):MutableLiveData<Int>
    {
        val mCheckUserExists = MutableLiveData<Int>()

        mFirestore.collection(COLLECTION_STUDENT)
            .whereEqualTo("mobile", phone).limit(1L).get().addOnCompleteListener { it1 ->

                if (it1.isSuccessful) {

                    if(it1.result.isEmpty)
                    {
                        mFirestore.collection(COLLECTION_TUTOR)
                            .whereEqualTo("mobile", phone).limit(1L).get().addOnCompleteListener {it2->
                                if (it2.isSuccessful) {
                                    if(!it2.result.isEmpty)
                                    mCheckUserExists.value=2
                                    else
                                        mCheckUserExists.value=0
                                } else {
                                    Log.d("user check","fail")

                                }
                            }
                    }
                    else
                    mCheckUserExists.value=1
                } else {
                    Log.d("user check","fail")
                }
            }

        return mCheckUserExists
    }

    fun uploadPictureToFirebase(imageUri: Uri,collection:String): MutableLiveData<Boolean> {

        val userID = mAuth.currentUser!!.uid
        val storageReference =
            FirebaseStorage.getInstance().getReference("/ProfilePictures/$userID")
        val uploadedMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl
                    .addOnSuccessListener {
                        mFirestore.collection(collection).document(userID)
                            .update("profilePicturePath", it.toString())
                            .addOnFailureListener {
                                Log.d("ishan","${it.message}")
                            }
                    }
            }
            .addOnProgressListener {
            }
            .addOnCompleteListener {
                uploadedMutableLiveData.value = true

            }
        return uploadedMutableLiveData

    }

    fun checkUserType(): MutableLiveData<Pair<Int,Boolean>> {

        val mCheckUserExists = MutableLiveData<Pair<Int,Boolean>>()

        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener { student->
                if(student.exists())
                {
                    val stu=student.toObject(Student::class.java)

                    mCheckUserExists.value=Pair(1,stu!!.profileIsComplete)
                }
                else
                {
                    mFirestore.collection(COLLECTION_TUTOR).document(mAuth.currentUser!!.uid).get()
                        .addOnSuccessListener{tutor->
                            if(tutor.exists())
                            {
                                val tut=tutor.toObject(Tutor::class.java)
                                mCheckUserExists.value=Pair(2,tut!!.profileIsComplete)
                            }
                    }
                        .addOnFailureListener {
                            mCheckUserExists.value=Pair(0,false)
                        }
                }
            }
            .addOnFailureListener {
                mCheckUserExists.value=Pair(0,false)
            }


        Log.d("ishan","${mCheckUserExists.value}")

        return mCheckUserExists

    }

    fun getStudent(): MutableLiveData<Student> {
        val mStudentLiveData = MutableLiveData<Student>()
        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                mStudentLiveData.value=it.toObject(Student::class.java)
            }

        return mStudentLiveData
    }

    fun saveStudent(student: Student): MutableLiveData<Boolean> {
       val mStudentStoreSuccess = MutableLiveData<Boolean>()

        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid).set(student)
            .addOnSuccessListener {
                mStudentStoreSuccess.value=true
            }
            .addOnFailureListener {
                mStudentStoreSuccess.value = false
            }




       return mStudentStoreSuccess


    }
    fun userSignOut() {
        mAuth.signOut()
    }

    fun getTutor(): MutableLiveData<Tutor> {

        val mTutorLiveData = MutableLiveData<Tutor>()
        mFirestore.collection(COLLECTION_TUTOR).document(mAuth.currentUser!!.uid).get()
            .addOnSuccessListener {
                mTutorLiveData.value=it.toObject(Tutor::class.java)
            }

        return mTutorLiveData

    }

    fun saveTutor(tutor: Tutor): MutableLiveData<Boolean> {

        val mTutorStoreSuccess = MutableLiveData<Boolean>()

        mFirestore.collection(COLLECTION_TUTOR).document(mAuth.currentUser!!.uid).set(tutor)
            .addOnSuccessListener {
                mTutorStoreSuccess.value=true
            }
            .addOnFailureListener {
                mTutorStoreSuccess.value = false
            }




        return mTutorStoreSuccess
    }
    fun getAllTutor():MutableLiveData<ArrayList<Tutor>> {
        val tutorList = MutableLiveData<ArrayList<Tutor>>()
        mFirestore.collection(COLLECTION_TUTOR)
            .whereEqualTo("profileIsComplete",true)
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                val arrayList= arrayListOf<Tutor>()
                for (snapshot in value!!) {

                    val order = snapshot.toObject(Tutor::class.java)


                    arrayList.add(order)
                }
                tutorList.value = arrayList
            } else {
          Log.d("fireStore", "tutor error: $error")
        }
            }


        return tutorList

    }

    fun updateFcmTokenListOfTutor(tutor: Tutor) {

        if (!tutor.fcmTokens.contains(FirebaseMessaging.getInstance().token.result)) {

            val reference = mFirestore.collection(COLLECTION_TUTOR)
                .document(mAuth.currentUser!!.uid)
            reference.update("tokenId", FirebaseMessaging.getInstance().token.result.toString())
            reference.update(
                "fcmTokens",
                FieldValue.arrayUnion(FirebaseMessaging.getInstance().token.result.toString())
            )
        }
    }
    fun updateFcmTokenListOfStudent(student: Student) {

        if (!student.fcmTokens.contains(FirebaseMessaging.getInstance().token.result)) {

            val reference = mFirestore.collection(COLLECTION_STUDENT)
                .document(mAuth.currentUser!!.uid)
            reference.update("tokenId", FirebaseMessaging.getInstance().token.result.toString())
            reference.update(
                "fcmTokens",
                FieldValue.arrayUnion(FirebaseMessaging.getInstance().token.result.toString())
            )
        }
    }

    fun getTutorByMobileNumber(mobile: String): MutableLiveData<Tutor> {

        val tutorLiveData = MutableLiveData<Tutor>()

        mFirestore.collection(COLLECTION_TUTOR).whereEqualTo("mobile",mobile)
            .get()
            .addOnSuccessListener {
                var tutor=Tutor()
                for (doc in it)
                {
                    tutor= doc.toObject(Tutor::class.java)
                }
                tutorLiveData.value=tutor
            }
            .addOnFailureListener {
                tutorLiveData.value = Tutor()
            }
            return tutorLiveData
    }

    fun getStudentByMobileNumber(mobile: String): MutableLiveData<Student> {

        val studentLiveData = MutableLiveData<Student>()

        mFirestore.collection(COLLECTION_STUDENT).whereEqualTo("mobile",mobile)
            .get()
            .addOnSuccessListener {
                var student=Student()
                for (doc in it)
                {
                    student= doc.toObject(Student::class.java)
                }
                studentLiveData.value=student
            }
            .addOnFailureListener {
                studentLiveData.value = Student()
            }
        return studentLiveData
    }

    fun sendTutorRequest(requestTutor: RequestTutor):MutableLiveData<Boolean>
    {
        val success=MutableLiveData<Boolean>()
        requestTutor.requestId="FTMREQ"+(System.currentTimeMillis()/1000).toString()
        val tutorStudentsRef= mFirestore.collection(COLLECTION_TUTOR).document(requestTutor.tutorId).collection(COLLECTION_TUTOR_STUDENTS).document(requestTutor.requestId)
        val studentTutorRef = mFirestore.collection(COLLECTION_STUDENT).document(requestTutor.studentId).collection(COLLECTION_STUDENT_TUTORS).document(requestTutor.requestId)

               mFirestore.runBatch{

                   it.set(tutorStudentsRef,requestTutor)
                   it.set(studentTutorRef,requestTutor)

        }

            .addOnSuccessListener {
                success.value=true
            }
            .addOnFailureListener {
                success.value=false
            }

        return success
    }

    fun getAllTutorRequests(): MutableLiveData<ArrayList<RequestTutor>> {

        val tutorRequests=MutableLiveData<ArrayList<RequestTutor>>()
        mFirestore.collection(COLLECTION_TUTOR).document(mAuth.currentUser!!.uid)
            .collection(COLLECTION_TUTOR_STUDENTS)
            .orderBy("timeOfRequest")
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<RequestTutor>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(RequestTutor::class.java)
                        eachRequest.requestId=snapshot.id


                        arrayList.add(eachRequest)
                    }
                    tutorRequests.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }
        return tutorRequests
    }
    fun getStudentById(studentId:String): MutableLiveData<Student> {
        val mStudentLiveData = MutableLiveData<Student>()
        mFirestore.collection(COLLECTION_STUDENT).document(studentId).get()
            .addOnSuccessListener {
                mStudentLiveData.value=it.toObject(Student::class.java)
            }

        return mStudentLiveData
    }

    fun approveStudentRequest(requestTutor: RequestTutor):MutableLiveData<Boolean>
    {
        val approvalSuccess=MutableLiveData<Boolean>()

        val tutorStudentsRef= mFirestore.collection(COLLECTION_TUTOR).document(requestTutor.tutorId).collection(COLLECTION_TUTOR_STUDENTS).document(requestTutor.requestId)
        val studentTutorRef = mFirestore.collection(COLLECTION_STUDENT).document(requestTutor.studentId).collection(COLLECTION_STUDENT_TUTORS).document(requestTutor.requestId)

        mFirestore.runBatch{

            it.update(tutorStudentsRef,"completed",true)
            it.update(studentTutorRef,"completed",true)
            it.update(tutorStudentsRef,"declined",false)
            it.update(studentTutorRef,"declined",false)
            it.update(tutorStudentsRef,"timeOfAcceptance",Timestamp.now())
            it.update(studentTutorRef,"timeOfAcceptance",Timestamp.now())

        }

            .addOnSuccessListener {
                approvalSuccess.value=true
            }
            .addOnFailureListener {
                approvalSuccess.value=false
            }

        return approvalSuccess
    }
    fun studentDeleteTutor(requestTutor: RequestTutor):MutableLiveData<Boolean>
    {
        val approvalSuccess=MutableLiveData<Boolean>()

        val tutorStudentsRef= mFirestore.collection(COLLECTION_TUTOR).document(requestTutor.tutorId).collection(COLLECTION_TUTOR_STUDENTS).document(requestTutor.requestId)
        val studentTutorRef = mFirestore.collection(COLLECTION_STUDENT).document(requestTutor.studentId).collection(COLLECTION_STUDENT_TUTORS).document(requestTutor.requestId)

        mFirestore.runBatch{


            it.update(tutorStudentsRef,"deleteByStudent",true)
            it.update(studentTutorRef,"deleteByStudent",true)

        }

            .addOnSuccessListener {
                approvalSuccess.value=true
            }
            .addOnFailureListener {
                approvalSuccess.value=false
            }

        return approvalSuccess
    }
    fun disapproveStudentRequest(requestTutor: RequestTutor):MutableLiveData<Boolean>
    {
        val disapproveSuccess=MutableLiveData<Boolean>()

        val tutorStudentsRef= mFirestore.collection(COLLECTION_TUTOR).document(requestTutor.tutorId).collection(COLLECTION_TUTOR_STUDENTS).document(requestTutor.requestId)
        val studentTutorRef = mFirestore.collection(COLLECTION_STUDENT).document(requestTutor.studentId).collection(COLLECTION_STUDENT_TUTORS).document(requestTutor.requestId)

        mFirestore.runBatch{

            it.update(tutorStudentsRef,"completed",true)
            it.update(studentTutorRef,"completed",true)
            it.update(tutorStudentsRef,"declined",true)
            it.update(studentTutorRef,"declined",true)
            it.update(tutorStudentsRef,"timeOfAcceptance",Timestamp.now())
            it.update(studentTutorRef,"timeOfAcceptance",Timestamp.now())


        }

            .addOnSuccessListener {
                disapproveSuccess.value=true
            }
            .addOnFailureListener {
                disapproveSuccess.value=false
            }

        return disapproveSuccess
    }

    fun getAllAcceptedStudentRequests(): MutableLiveData<ArrayList<RequestTutor>> {

        val tutorRequests=MutableLiveData<ArrayList<RequestTutor>>()
        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid)
            .collection(COLLECTION_STUDENT_TUTORS)
            .whereEqualTo("completed",true)
            .whereEqualTo("declined",false)
            .whereEqualTo("deleteByStudent",false)
            .orderBy("timeOfRequest")
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<RequestTutor>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(RequestTutor::class.java)
                        eachRequest.requestId=snapshot.id


                        arrayList.add(eachRequest)
                    }
                    tutorRequests.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }
        return tutorRequests

    }

    fun getAllRejectedStudentRequests(): MutableLiveData<ArrayList<RequestTutor>> {

        val tutorRequests=MutableLiveData<ArrayList<RequestTutor>>()
        mFirestore.collection(COLLECTION_STUDENT).document(mAuth.currentUser!!.uid)
            .collection(COLLECTION_STUDENT_TUTORS)
            .orderBy("timeOfRequest")
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<RequestTutor>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(RequestTutor::class.java)
                        eachRequest.requestId=snapshot.id

                        if(eachRequest.deleteByStudent)
                            arrayList.add(eachRequest)
                        else
                        {
                            if(eachRequest.isCompleted && !eachRequest.isDeclined)
                                continue
                            else
                            arrayList.add(eachRequest)
                        }


                    }
                    tutorRequests.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }
        return tutorRequests
    }

    fun getTutorById(tutorId: String): MutableLiveData<Tutor> {

        val tutorLiveData = MutableLiveData<Tutor>()
        mFirestore.collection(COLLECTION_TUTOR).document(tutorId)
            .get()
            .addOnSuccessListener {
                val tutor = it.toObject(Tutor::class.java)!!

                tutorLiveData.value=tutor
            }
            .addOnFailureListener {
                tutorLiveData.value = Tutor()
            }
        return tutorLiveData
    }

    fun storeDoubt(imageUri:Uri?,doubtInfo: DoubtInfo):MutableLiveData<Boolean>
    {
        val success=MutableLiveData<Boolean>()
        val userID = mAuth.currentUser!!.uid
        val storageReference =
            FirebaseStorage.getInstance().getReference("/DoubtImages/${userID.substring(0,5)+(System.currentTimeMillis()/1000).toString()}")

        if (imageUri!=null)
        {
            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.downloadUrl
                        .addOnSuccessListener {
                            doubtInfo.doubtImagePath=it.toString()
                            mFirestore.collection(COLLECTION_DOUBT).document(doubtInfo.doubtId)
                                .set(doubtInfo)
                                .addOnSuccessListener {
                                    success.value=true
                                }
                                .addOnFailureListener {
                                    success.value=false
                                    Log.d("ishan","${it.message}")
                                }
                        }
                }
                .addOnFailureListener{
                    success.value=false

                }
        }
        else
        {
            mFirestore.collection(COLLECTION_DOUBT).document(doubtInfo.doubtId)
                .set(doubtInfo)
                .addOnSuccessListener {
                    success.value=true
                }
                .addOnFailureListener {
                    success.value=false
                    Log.d("ishan","${it.message}")
                }

        }
        return success
    }

    fun getAllDoubtOfCurrentStudent(context:Context): MutableLiveData<ArrayList<DoubtInfo>> {

        val allStudentDoubts=MutableLiveData<ArrayList<DoubtInfo>>()
        runBlocking {
            val job = launch { LocalDataRepo(context).deleteAllDoubts() }
            job.join()
        }
        mFirestore.collection(COLLECTION_DOUBT)
            .whereEqualTo("studentId",FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy("createdOn",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<DoubtInfo>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(DoubtInfo::class.java)

                        arrayList.add(eachRequest)
                        runBlocking {
                            val job = this.async {
                                LocalDataRepo(context).insertDoubts(eachRequest)
                            }
                            job.await()
                        }
                    }
                    allStudentDoubts.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }
        return allStudentDoubts
    }

    fun getAllDoubts(): MutableLiveData<ArrayList<DoubtInfo>> {

        val allDoubts=MutableLiveData<ArrayList<DoubtInfo>>()
        mFirestore.collection(COLLECTION_DOUBT)
            .whereEqualTo("closed",false)
            .orderBy("createdOn",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<DoubtInfo>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(DoubtInfo::class.java)

                        arrayList.add(eachRequest)
                    }
                    Log.d("ishan123","${arrayList}")
                    allDoubts.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }

        return allDoubts

    }

    fun markDoubtAsDone(doubtId:String):MutableLiveData<Boolean>
    {
        val success=MutableLiveData<Boolean>()
            mFirestore.collection(COLLECTION_DOUBT).document(doubtId)
                .update("closed",true)
                .addOnSuccessListener {
                    success.value=true
                }
                .addOnFailureListener {
                    success.value=false
                }

        return success

    }

    fun markDoubtAsUnDone(doubtId:String):MutableLiveData<Boolean>
    {
        val success=MutableLiveData<Boolean>()
        mFirestore.collection(COLLECTION_DOUBT).document(doubtId)
            .update("closed",false)
            .addOnSuccessListener {
                success.value=true
            }
            .addOnFailureListener {
                success.value=false
            }

        return success

    }


    fun storeSolution(imageUri: Uri?, solutionInfo: SolutionInfo,update:Boolean): MutableLiveData<Boolean> {

        val success=MutableLiveData<Boolean>()
        val userID = mAuth.currentUser!!.uid
        val storageReference =
            FirebaseStorage.getInstance().getReference("/SolutionImages/${userID.substring(0,5)+(System.currentTimeMillis()/1000).toString()}")
        val solutionReference= mFirestore.collection(COLLECTION_SOLUTIONS)
            .document(solutionInfo.solutionId)
        val doubtReference = mFirestore.collection(COLLECTION_DOUBT).document(solutionInfo.doubtId)
        if (imageUri!=null)
        {
            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.downloadUrl
                        .addOnSuccessListener {
                            solutionInfo.solutionImagePath=it.toString()
                           mFirestore.runBatch { writeBatch ->

                               writeBatch.set(solutionReference, solutionInfo)
                               if(!update)
                               {
                                   writeBatch.update(
                                       doubtReference,
                                       "noOfSolutions",
                                       FieldValue.increment(1)
                                   )
                               }


                           }.addOnSuccessListener {
                                    success.value=true
                                }
                                .addOnFailureListener {
                                    success.value=false
                                    Log.d("ishan","${it.message}")
                                }
                        }
                }
                .addOnFailureListener{
                    success.value=false

                }
        }
        else
        {
            mFirestore.runBatch { writeBatch ->

                writeBatch.set(solutionReference, solutionInfo)
                if(!update)
                {
                    writeBatch.update(
                        doubtReference,
                        "noOfSolutions",
                        FieldValue.increment(1)
                    )
                }

            }
                .addOnSuccessListener {
                    success.value=true
                }
                .addOnFailureListener {
                    success.value=false
                    Log.d("ishan","${it.message}")
                }

        }

        return success
    }

    fun getAllTutorSolutions(): MutableLiveData<ArrayList<SolutionInfo>> {
        val allSolutions=MutableLiveData<ArrayList<SolutionInfo>>()
        mFirestore.collection(COLLECTION_SOLUTIONS)
            .whereEqualTo("studentId",mAuth.currentUser!!.uid)
            .orderBy("solvedOn",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<SolutionInfo>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(SolutionInfo::class.java)

                            arrayList.add(eachRequest)
                    }

                    allSolutions.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }
        return allSolutions
    }

    //for tutors
    fun getMySolutions(): MutableLiveData<ArrayList<SolutionInfo>> {
        val allSolutions=MutableLiveData<ArrayList<SolutionInfo>>()
        mFirestore.collection(COLLECTION_SOLUTIONS)
            .whereEqualTo("tutorId",mAuth.currentUser!!.uid)
            .orderBy("solvedOn",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<SolutionInfo>()
                    for (snapshot in value!!) {

                        val eachRequest= snapshot.toObject(SolutionInfo::class.java)

                        arrayList.add(eachRequest)
                    }
                    allSolutions.value = arrayList
                } else {
                    Log.d("fireStore", "tutor error: $error")
                }
            }
        return allSolutions
    }

    fun sendMessage(messages: Messages,chattingHelper: ChattingHelper)
    {
       val chatRef= mFirestore.collection(COLLECTION_CHATS).document(messages.messageId)

        val messageRef=mFirestore.collection(COLLECTION_CHATS).document(messages.messageId).collection(COLLECTION_CHATS_MESSAGES).document()
                mFirestore.runBatch {
                    it.set(chatRef, chattingHelper, SetOptions.merge())
                    it.set(messageRef,messages)
                }
            .addOnSuccessListener {
                Log.d("chatting","success")
            }
            .addOnFailureListener {
                Log.d("chatting","error ${it.message}")
            }
    }
    fun getMessages(chatId:String):MutableLiveData<ArrayList<Messages>>
    {
        val prevMessages=MutableLiveData<ArrayList<Messages>>()
        mFirestore.collection(COLLECTION_CHATS).document(chatId).collection(COLLECTION_CHATS_MESSAGES)
            .orderBy("time")
            .addSnapshotListener{ value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<Messages>()
                    for (snapshot in value!!) {

                        val message= snapshot.toObject(Messages::class.java)

                        arrayList.add(message)
                    }
                    prevMessages.value = arrayList
                } else {
                    Log.d("fireStore", "chat error: $error")
                }
        }
        return prevMessages
    }

    fun getPeopleIHaveChatWith():MutableLiveData<ArrayList<ChattingHelper>>
    {
        val peopleChattedWith=MutableLiveData<ArrayList<ChattingHelper>>()
        mFirestore.collection(COLLECTION_CHATS)
            .orderBy("lastContact")
            .addSnapshotListener{ value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<ChattingHelper>()
                    for (snapshot in value!!) {

                        val chattingHelper= snapshot.toObject(ChattingHelper::class.java)
                        if(chattingHelper.tutorId==FirebaseAuth.getInstance().currentUser!!.uid
                            || chattingHelper.studentId==FirebaseAuth.getInstance().currentUser!!.uid)
                        arrayList.add(chattingHelper)


                    }

                    peopleChattedWith.value = arrayList
                    Log.d("chatting","${arrayList} ${FirebaseAuth.getInstance().currentUser!!.uid}")
                } else {
                    Log.d("fireStore", "chat error: $error")
                }
            }
        return peopleChattedWith
    }

    fun updateRatingOfTutor(tutorId:String,ratings:ArrayList<Int>)
    {
        mFirestore.collection(COLLECTION_TUTOR).document(tutorId).update("rating",ratings)
            .addOnSuccessListener {
                Log.d("ratings update","successful")
            }
            .addOnFailureListener {
                Log.d("ratings update error","${it.message}")
            }
    }
    fun getListOfAllAcceptedStudents(tutorId: String):MutableLiveData<ArrayList<String>>
    {
        val studentIdList=MutableLiveData<ArrayList<String>>()
        mFirestore.collection(COLLECTION_TUTOR).document(tutorId).collection(COLLECTION_TUTOR_STUDENTS)
            .whereEqualTo("completed",true)
            .whereEqualTo("declined",false)
            .get()
            .addOnSuccessListener {
                val arrayList= arrayListOf<String>()
                for (snapshot in it!!) {

                    val request = snapshot.toObject(RequestTutor::class.java)

                    arrayList.add(request.studentId)
                }

                studentIdList.value = arrayList
            }
            .addOnFailureListener {

                Log.d("fireStore", "student error: ${it.message}")

            }
        return studentIdList
    }
    fun getListOfAllAcceptedStudentsRequestTutor(tutorId: String):MutableLiveData<ArrayList<RequestTutor>>
    {
        val studentIdList=MutableLiveData<ArrayList<RequestTutor>>()
        mFirestore.collection(COLLECTION_TUTOR).document(tutorId).collection(COLLECTION_TUTOR_STUDENTS)
            .whereEqualTo("completed",true)
            .whereEqualTo("declined",false)
            .whereEqualTo("deleteByStudent",false)
            .get()
            .addOnSuccessListener {
                val arrayList= arrayListOf<RequestTutor>()
                for (snapshot in it!!) {

                    val request = snapshot.toObject(RequestTutor::class.java)

                    arrayList.add(request)
                }

                studentIdList.value = arrayList
                Log.d("fireStore", "${arrayList}")
            }
            .addOnFailureListener {

                Log.d("fireStore", "student error: ${it.message}")

            }
        return studentIdList
    }
    fun getListOfAllAcceptedOrRequestPendingStudents(tutorId: String):MutableLiveData<ArrayList<String>>
    {
        val studentIdList=MutableLiveData<ArrayList<String>>()
        mFirestore.collection(COLLECTION_TUTOR).document(tutorId).collection(COLLECTION_TUTOR_STUDENTS)
            .get()
            .addOnSuccessListener {
                val arrayList= arrayListOf<String>()
                for (snapshot in it!!) {

                    val request = snapshot.toObject(RequestTutor::class.java)
                    if(!request.isDeclined && !request.deleteByStudent)
                    arrayList.add(request.studentId)
                }

                studentIdList.value = arrayList
            }
            .addOnFailureListener {

                    Log.d("fireStore", "student error: ${it.message}")

            }






        return studentIdList
    }
    fun getAllStudents(listOfAcceptedStudents:MutableList<String>): MutableLiveData<ArrayList<Student>> {

        val studentList = MutableLiveData<ArrayList<Student>>()
        mFirestore.collection(COLLECTION_STUDENT)
            .whereIn("studentId",listOfAcceptedStudents)
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<Student>()
                    for (snapshot in value!!) {

                        val order = snapshot.toObject(Student::class.java)


                        arrayList.add(order)
                    }
                    studentList.value = arrayList
                    Log.d("anshu","${arrayList}")
                } else {
                    Log.d("fireStore", "student error: $error")
                }
            }


        return studentList

    }

    fun storeReview(ratingInfo: RatingInfo)
    {
         mFirestore.collection(COLLECTION_RATINGS).add(ratingInfo)
            .addOnSuccessListener {
                Log.d("rating storage","success")
            }
            .addOnFailureListener {
                Log.d("rating storage","error ${it.message}")
            }
    }

    //get ratings given by me
    fun getRatingsGivenByMe():MutableLiveData<ArrayList<RatingInfo>>
    {
        val ratingsList = MutableLiveData<ArrayList<RatingInfo>>()
        mFirestore.collection(COLLECTION_RATINGS)
            .whereEqualTo("ratedBy",FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy("ratedOn")
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<RatingInfo>()
                    for (snapshot in value!!) {

                        val order = snapshot.toObject(RatingInfo::class.java)


                        arrayList.add(order)
                    }
                    ratingsList.value = arrayList
                } else {
                    Log.d("fireStore", "ratings list error: $error")
                }
            }


        return ratingsList
    }

    //get ratings given by me
    fun getRatingsReceivedByMe():MutableLiveData<ArrayList<RatingInfo>>
    {
        val ratingsList = MutableLiveData<ArrayList<RatingInfo>>()
        mFirestore.collection(COLLECTION_RATINGS)
            .whereEqualTo("ratedTo",FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy("ratedOn")
            .addSnapshotListener { value, error ->
                if (error == null)
                {

                    val arrayList= arrayListOf<RatingInfo>()
                    for (snapshot in value!!) {

                        val order = snapshot.toObject(RatingInfo::class.java)


                        arrayList.add(order)
                    }
                    ratingsList.value = arrayList
                    Log.d("rating","${ratingsList.value}")
                } else {
                    Log.d("fireStore", "ratings list error: $error")
                }
            }


        return ratingsList
    }

    fun addUpiId(upiId: String): MutableLiveData<Boolean> {
        val addingSuccess=MutableLiveData<Boolean>()
        mFirestore.collection(COLLECTION_TUTOR).document(FirebaseAuth.getInstance().currentUser!!.uid).update("upiId",upiId)
            .addOnSuccessListener {
                Log.d("upi update","successful")
                addingSuccess.value=true
            }
            .addOnFailureListener {
                Log.d("upi update","${it.message}")
                addingSuccess.value=false
            }


        return addingSuccess
    }

    fun storeTransaction(transactionInfo: TransactionInfo): MutableLiveData<Boolean> {

        val addingSuccess=MutableLiveData<Boolean>()
        mFirestore.collection(COLLECTION_TRANSACTIONS).document(transactionInfo.transactionId).set(transactionInfo)
            .addOnSuccessListener {

                addingSuccess.value=true
            }
            .addOnFailureListener {
                Log.d("storage error","${it.message}")
                addingSuccess.value=false
            }


        return addingSuccess

    }

    fun getAllTransactionsStudent():MutableLiveData<MutableList<TransactionInfo>>
    {
        val transactionList=MutableLiveData<MutableList<TransactionInfo>>()

        mFirestore.collection(COLLECTION_TRANSACTIONS)
            .whereEqualTo("studentId",FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy("completedOn")
            .addSnapshotListener { value, error ->
                if (error == null) {

                    val arrayList = arrayListOf<TransactionInfo>()
                    for (snapshot in value!!) {

                        val order = snapshot.toObject(TransactionInfo::class.java)


                        arrayList.add(order)
                    }
                    transactionList.value = arrayList
                    Log.d("transaction", "${transactionList.value}")
                } else {
                    Log.d("transaction", "ratings list error: $error")
                }
            }

        return transactionList


    }

    fun getAllTransactionsTutor():MutableLiveData<MutableList<TransactionInfo>>
    {
        val transactionList=MutableLiveData<MutableList<TransactionInfo>>()

        mFirestore.collection(COLLECTION_TRANSACTIONS)
            .whereEqualTo("tutorId",FirebaseAuth.getInstance().currentUser!!.uid)
            .orderBy("completedOn")
            .addSnapshotListener { value, error ->
                if (error == null) {

                    val arrayList = arrayListOf<TransactionInfo>()
                    for (snapshot in value!!) {

                        val order = snapshot.toObject(TransactionInfo::class.java)


                        arrayList.add(order)
                    }
                    transactionList.value = arrayList
                    Log.d("transaction", "${transactionList.value}")
                } else {
                    Log.d("transaction", "ratings list error: $error")
                }
            }

        return transactionList


    }

}