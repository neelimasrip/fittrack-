import { initializeApp } from "firebase/app";
import { 
  getAuth, 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword,
  signOut,
  GoogleAuthProvider,
  signInWithPopup,
  sendPasswordResetEmail
} from "firebase/auth";
import { 
  getFirestore, 
  doc, 
  getDoc, 
  setDoc, 
  updateDoc 
} from "firebase/firestore";

const firebaseConfig = {
  apiKey: "AIzaSyDIHXw1lZQKlMDYtRl6ndT0Yd8Qb5cva_Y",
  authDomain: "fittrack-5b6aa.firebaseapp.com",
  projectId: "fittrack-5b6aa",
  storageBucket: "fittrack-5b6aa.firebasestorage.app",
  messagingSenderId: "524815681692",
  appId: "1:524815681692:android:c066253e175c9385c74559"
};

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app);

export {
  signInWithEmailAndPassword,
  createUserWithEmailAndPassword,
  signOut,
  GoogleAuthProvider,
  signInWithPopup,
  sendPasswordResetEmail,
  doc,
  getDoc,
  setDoc,
  updateDoc
};
