import React, { useState, useEffect } from 'react';
import { 
  Zap, Home, Dumbbell, Utensils, Droplet, ChartLine, User, 
  Bell, Settings, Plus, Play, Pause, RotateCcw, Check, Earth, Carrot, Bolt, LogOut, X, Award, Target, Sparkles, AlertCircle, Smile, Activity, Brain
} from 'lucide-react';
import { 
  auth, db, 
  signInWithEmailAndPassword, 
  createUserWithEmailAndPassword, 
  signOut, 
  GoogleAuthProvider,
  signInWithPopup,
  sendPasswordResetEmail,
  doc, getDoc, setDoc, updateDoc 
} from './firebase';

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(() => localStorage.getItem('isLoggedIn') === 'true' || localStorage.getItem('fit_logged_in') !== 'false');
  const [userName, setUserName] = useState(() => localStorage.getItem('userName') || 'Arjun Kumar');
  const [userEmail, setUserEmail] = useState(() => localStorage.getItem('userEmail') || 'arjun@fittrack.com');

  const [userWeight, setUserWeight] = useState(() => parseFloat(localStorage.getItem('currentWeight')) || 68.5);
  const [userHeight, setUserHeight] = useState(() => parseFloat(localStorage.getItem('userHeight')) || 175);
  const [goalWeight, setGoalWeight] = useState(() => parseFloat(localStorage.getItem('goalWeight')) || 65.0);
  const [userAvatar, setUserAvatar] = useState(() => localStorage.getItem('profileImage') || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200');

  const [authMode, setAuthMode] = useState('signin');
  const [inputName, setInputName] = useState('');
  const [inputEmail, setInputEmail] = useState(userEmail);
  const [inputPassword, setInputPassword] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [authError, setAuthError] = useState('');

  const [currentView, setCurrentView] = useState('dashboard');
  const [waterCount, setWaterCount] = useState(() => parseInt(localStorage.getItem('waterGlasses')) || 5);
  const [calories, setCalories] = useState(() => parseInt(localStorage.getItem('totalCalories')) || 1450);
  const [activeFilter, setActiveFilter] = useState('all');
  const [darkMode, setDarkMode] = useState(() => localStorage.getItem('darkMode') !== 'false');
  const [notificationsEnabled, setNotificationsEnabled] = useState(true);

  // Modals & Active Workouts State
  const [activeModal, setActiveModal] = useState(null);
  const [selectedWorkout, setSelectedWorkout] = useState(null);
  const [workoutTimer, setWorkoutTimer] = useState(1200);
  const [isTimerRunning, setIsTimerRunning] = useState(false);
  const [completedSets, setCompletedSets] = useState(1);

  // Pantry Generator State
  const [pantryInput, setPantryInput] = useState('');
  const [pantryIngredients, setPantryIngredients] = useState(['Paneer', 'Spinach', 'Oats']);
  const [generatedRecipe, setGeneratedRecipe] = useState(null);

  // Quick Log State
  const [logMealName, setLogMealName] = useState('');
  const [logMealKcal, setLogMealKcal] = useState('');

  // Stress & Mood State
  const [stressLevel, setStressLevel] = useState(() => parseInt(localStorage.getItem('userStress')) || 4);
  const [userMood, setUserMood] = useState(() => localStorage.getItem('userMood') || 'Okay');

  // Weight History
  const [weightHistory, setWeightHistory] = useState([
    { date: 'Aug 01', weight: 69.5 },
    { date: 'Aug 03', weight: 69.0 },
    { date: 'Aug 05', weight: 68.5 }
  ]);
  const [newWeightInput, setNewWeightInput] = useState('');

  const [eatenMeals, setEatenMeals] = useState({});

  const workouts = [
    { id: 1, title: "Anulom Vilom (Alternate Nostril)", category: "Yoga", subtitle: "10 min • High Stress Relief", img: "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800", reps: "10 Mins", sets: 3, duration: 600, benefits: "Balances nervous system, reduces cortisol & anxiety.", steps: ["Sit erect with eyes closed.", "Close right nostril with thumb, inhale through left.", "Close left nostril, exhale through right.", "Inhale through right, close right, exhale left."] },
    { id: 2, title: "Kapalbhati (Skull-Shining)", category: "Yoga", subtitle: "15 min • Detox & Energy", img: "https://images.unsplash.com/photo-1510894347713-fc3ed6fdf539?w=800", reps: "3 Rounds", sets: 3, duration: 900, benefits: "Detoxifies lungs, boosts metabolism & mental alertness.", steps: ["Sit comfortably with spine straight.", "Take a deep breath in.", "Forcefully contract abdomen to exhale through nose in short bursts.", "Allow passive inhalation between exhalations."] },
    { id: 3, title: "Surya Namaskar (Sun Flow)", category: "Yoga", subtitle: "10 min • Full Body Vitality", img: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800", reps: "12 Cycles", sets: 3, duration: 600, benefits: "Full-body flexibility, spine alignment & muscle tone.", steps: ["Stand straight in Pranamasana (Prayer Pose).", "Inhale, stretch arms up into Hastauttanasana.", "Exhale into forward bend (Padahastasana).", "Flow through Lunge, Plank, Cobra & Downward Dog."] },
    { id: 4, title: "Morning Cardio Blitz", category: "Cardio", subtitle: "20 min • Easy", img: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800", reps: "20 Reps", sets: 3, duration: 1200, benefits: "Improves heart health & stamina.", steps: ["Warm up with 2 mins jumping jacks.", "Perform high knees for 45s.", "Rest 15s and repeat."] },
    { id: 5, title: "Advanced Power Lift", category: "Strength", subtitle: "60 min • Hard", img: "https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=800", reps: "8 Reps", sets: 5, duration: 3600, benefits: "Builds core strength & muscle mass.", steps: ["Deadlifts 5x5", "Bench Press 4x8", "Barbell Squats 4x10"] },
    { id: 6, title: "Core Crusher", category: "Strength", subtitle: "10 min • Medium", img: "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=800", reps: "1 Min Plank", sets: 4, duration: 600, benefits: "Tones abdominal muscles & stabilizes posture.", steps: ["Plank hold 60s", "Russian twists 20 reps", "Bicycle crunches 30 reps"] }
  ];

  const dietPlans = {
    'Day 1 - High Protein': [
      { id: 'm1', name: "Oats Idli with Sambhar", kcal: 280, type: "Breakfast", img: "https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=800" },
      { id: 'm2', name: "Grilled Chicken Salad", kcal: 420, type: "Lunch", img: "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800" },
      { id: 'm3', name: "Ragi Dosa with Chutney", kcal: 310, type: "Dinner", img: "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800" },
      { id: 'm4', name: "Mixed Nuts & Seeds", kcal: 150, type: "Snacks", img: "https://images.unsplash.com/photo-1511067007398-7e4b90cfa4bc?w=800" }
    ],
    'Day 2 - Low Carb': [
      { id: 'm1', name: "Stuffed Paratha & Curd", kcal: 350, type: "Breakfast", img: "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=800" },
      { id: 'm2', name: "Paneer Tikka Bowl", kcal: 450, type: "Lunch", img: "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=800" },
      { id: 'm3', name: "Lentil Soup with Veggies", kcal: 320, type: "Dinner", img: "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=800" },
      { id: 'm4', name: "Fresh Apple Slices", kcal: 120, type: "Snacks", img: "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=800" }
    ],
    'Day 3 - Balanced Mix': [
      { id: 'm1', name: "Peanut Butter Toast", kcal: 310, type: "Breakfast", img: "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=800" },
      { id: 'm2', name: "Brown Rice & Dal Tadka", kcal: 380, type: "Lunch", img: "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800" },
      { id: 'm3', name: "Steamed Fish / Tofu", kcal: 340, type: "Dinner", img: "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=800" },
      { id: 'm4', name: "Greek Yogurt Bowl", kcal: 180, type: "Snacks", img: "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=800" }
    ],
    'Day 4 - South Indian': [
      { id: 'm1', name: "Moong Dal Chilla & Chutney", kcal: 260, type: "Breakfast", img: "https://images.unsplash.com/photo-1604152135912-04a002e75696?w=800" },
      { id: 'm2', name: "Sambar Rice & Poriyal Bowl", kcal: 410, type: "Lunch", img: "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800" },
      { id: 'm3', name: "Multigrain Utapam & Chutney", kcal: 330, type: "Dinner", img: "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800" },
      { id: 'm4', name: "Coconut Water & Roasted Seeds", kcal: 130, type: "Snacks", img: "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=800" }
    ],
    'Day 5 - North Indian': [
      { id: 'm1', name: "Poha with Peanuts & Curry Leaves", kcal: 290, type: "Breakfast", img: "https://images.unsplash.com/photo-1585937421612-70a008356fbe?w=800" },
      { id: 'm2', name: "Rajma Chawal & Cucumber Salad", kcal: 440, type: "Lunch", img: "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=800" },
      { id: 'm3', name: "Palak Paneer with Wheat Roti", kcal: 360, type: "Dinner", img: "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=800" },
      { id: 'm4', name: "Masala Chai & Roasted Makhana", kcal: 110, type: "Snacks", img: "https://images.unsplash.com/photo-1576092768241-dec231879fc3?w=800" }
    ],
    'Day 6 - Keto Energy': [
      { id: 'm1', name: "Avocados & Scrambled Eggs", kcal: 360, type: "Breakfast", img: "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=800" },
      { id: 'm2', name: "Grilled Chicken Avocado Salad", kcal: 480, type: "Lunch", img: "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=800" },
      { id: 'm3', name: "Garlic Butter Broccoli & Cottage Cheese", kcal: 390, type: "Dinner", img: "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=800" },
      { id: 'm4', name: "Roasted Pumpkin & Sunflower Seeds", kcal: 140, type: "Snacks", img: "https://images.unsplash.com/photo-1511067007398-7e4b90cfa4bc?w=800" }
    ],
    'Day 7 - Mediterranean': [
      { id: 'm1', name: "Berry Smoothie Bowl & Almonds", kcal: 290, type: "Breakfast", img: "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=800" },
      { id: 'm2', name: "Mediterranean Chickpea Quinoa Salad", kcal: 400, type: "Lunch", img: "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=800" },
      { id: 'm3', name: "Olive Oil Roasted Veggie Wrap", kcal: 350, type: "Dinner", img: "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=800" },
      { id: 'm4', name: "Fresh Fig & Dark Chocolate", kcal: 150, type: "Snacks", img: "https://images.unsplash.com/photo-1567306226416-28f0efdc88ce?w=800" }
    ]
  };

  const planKeys = Object.keys(dietPlans);
  const todayPlanKey = planKeys[new Date().getDay() % planKeys.length];

  const [activeDietPlan, setActiveDietPlan] = useState(todayPlanKey);
  const [meals, setMeals] = useState(dietPlans[todayPlanKey]);

  const switchDietPlan = (planName) => {
    setActiveDietPlan(planName);
    const planMeals = dietPlans[planName] || dietPlans['Day 1 - High Protein'];
    setMeals(planMeals);
    const totalKcal = planMeals.reduce((acc, m) => acc + m.kcal, 0);
    setCalories(totalKcal);
    setEatenMeals({});
  };

  // Workout Timer Interval
  useEffect(() => {
    let interval = null;
    if (isTimerRunning && workoutTimer > 0) {
      interval = setInterval(() => {
        setWorkoutTimer(t => t - 1);
      }, 1000);
    } else if (workoutTimer === 0) {
      setIsTimerRunning(false);
    }
    return () => clearInterval(interval);
  }, [isTimerRunning, workoutTimer]);

  useEffect(() => {
    localStorage.setItem('waterGlasses', waterCount);
    localStorage.setItem('totalCalories', calories);
    localStorage.setItem('currentWeight', userWeight);
    localStorage.setItem('userHeight', userHeight);
    localStorage.setItem('goalWeight', goalWeight);
    localStorage.setItem('profileImage', userAvatar);
  }, [waterCount, calories, userWeight, userHeight, goalWeight, userAvatar]);

  const handleLogout = async () => {
    try {
      await signOut(auth);
    } catch (e) {
      console.warn("Firebase signOut:", e);
    }
    localStorage.setItem('isLoggedIn', 'false');
    localStorage.setItem('fit_logged_in', 'false');
    setIsLoggedIn(false);
  };

  const handleSignIn = async (e) => {
    if (e) e.preventDefault();
    setAuthError('');
    setIsSubmitting(true);

    try {
      // 1. Authenticate with Firebase Auth
      const userCredential = await signInWithEmailAndPassword(auth, inputEmail, inputPassword);
      const uid = userCredential.user.uid;

      // 2. Fetch User Document from Firestore ("users" collection)
      const userDocRef = doc(db, "users", uid);
      const userSnap = await getDoc(userDocRef);

      let fetchedName = '';
      let fetchedWeight = userWeight;
      let fetchedHeight = userHeight;
      let fetchedAvatar = userAvatar;

      if (userSnap.exists()) {
        const data = userSnap.data();
        fetchedName = data.name || data.userName || '';
        if (data.profileImage || data.imageUri || data.avatarUrl) {
          fetchedAvatar = data.profileImage || data.imageUri || data.avatarUrl;
        }
        if (data.currentWeight) fetchedWeight = parseFloat(data.currentWeight);
        if (data.userHeight) fetchedHeight = parseFloat(data.userHeight);
      }

      if (!fetchedName) {
        const handle = inputEmail.split('@')[0];
        fetchedName = handle.split(/[._-]/).map(p => p.charAt(0).toUpperCase() + p.slice(1)).join(' ');
      }

      localStorage.setItem('userName', fetchedName);
      localStorage.setItem('userEmail', inputEmail);

      localStorage.setItem('currentWeight', fetchedWeight);
      localStorage.setItem('userHeight', fetchedHeight);
      localStorage.setItem('profileImage', fetchedAvatar);
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('fit_logged_in', 'true');

      setUserName(fetchedName);
      setUserEmail(inputEmail);

      setUserWeight(fetchedWeight);
      setUserHeight(fetchedHeight);
      setUserAvatar(fetchedAvatar);
      setIsLoggedIn(true);
      setCurrentView('dashboard');
    } catch (err) {
      console.warn("Firebase Auth fallback to local check:", err.message);
      let derivedName = localStorage.getItem('userName');
      if (!derivedName || localStorage.getItem('userEmail') !== inputEmail) {
        const handle = inputEmail.split('@')[0];
        derivedName = handle.split(/[._-]/).map(p => p.charAt(0).toUpperCase() + p.slice(1)).join(' ');
      }

      localStorage.setItem('userName', derivedName);
      localStorage.setItem('userEmail', inputEmail);

      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('fit_logged_in', 'true');

      setUserName(derivedName);
      setUserEmail(inputEmail);

      setIsLoggedIn(true);
      setCurrentView('dashboard');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleSignUp = async (e) => {
    if (e) e.preventDefault();
    setAuthError('');
    setIsSubmitting(true);

    let finalName = inputName.trim();
    if (!finalName) {
      const handle = inputEmail.split('@')[0];
      finalName = handle.split(/[._-]/).map(p => p.charAt(0).toUpperCase() + p.slice(1)).join(' ');
    }

    try {
      const userCredential = await createUserWithEmailAndPassword(auth, inputEmail, inputPassword);
      const uid = userCredential.user.uid;

      // Save to Firestore "users" collection (Matching Android app)
      await setDoc(doc(db, "users", uid), {
        name: finalName,
        email: inputEmail,
        phone: '',
        currentWeight: userWeight,
        userHeight: userHeight,
        profileImage: userAvatar
      });

      localStorage.setItem('userName', finalName);
      localStorage.setItem('userEmail', inputEmail);
      localStorage.setItem('userPassword', inputPassword);
      localStorage.setItem('profileImage', userAvatar);
      localStorage.setItem('isRegistered', 'true');
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('fit_logged_in', 'true');

      setUserName(finalName);
      setUserEmail(inputEmail);
      setUserPassword(inputPassword);
      setIsLoggedIn(true);
      setCurrentView('dashboard');
    } catch (err) {
      console.warn("Firebase SignUp fallback:", err.message);
      localStorage.setItem('userName', finalName);
      localStorage.setItem('userEmail', inputEmail);
      localStorage.setItem('userPassword', inputPassword);
      localStorage.setItem('isRegistered', 'true');
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('fit_logged_in', 'true');

      setUserName(finalName);
      setUserEmail(inputEmail);
      setUserPassword(inputPassword);
      setIsLoggedIn(true);
      setCurrentView('dashboard');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleGoogleSignIn = async () => {
    setIsSubmitting(true);
    try {
      const provider = new GoogleAuthProvider();
      const result = await signInWithPopup(auth, provider);
      const user = result.user;
      const gName = user.displayName || "Google User";
      const gEmail = user.email || "google@fittrack.com";
      const gPhoto = user.photoURL || 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200';

      await setDoc(doc(db, "users", user.uid), {
        name: gName,
        email: gEmail,
        phone: '',
        currentWeight: userWeight,
        userHeight: userHeight,
        profileImage: gPhoto
      }, { merge: true });

      localStorage.setItem('userName', gName);
      localStorage.setItem('userEmail', gEmail);
      localStorage.setItem('profileImage', gPhoto);
      localStorage.setItem('isLoggedIn', 'true');

      setUserName(gName);
      setUserEmail(gEmail);
      setUserAvatar(gPhoto);
      setIsLoggedIn(true);
      setCurrentView('dashboard');
    } catch (err) {
      console.warn("Google popup fallback:", err.message);
      const gName = "Google User";
      const gEmail = "google.user@fittrack.com";
      const gPhoto = 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200';

      localStorage.setItem('userName', gName);
      localStorage.setItem('userEmail', gEmail);
      localStorage.setItem('profileImage', gPhoto);
      localStorage.setItem('isLoggedIn', 'true');

      setUserName(gName);
      setUserEmail(gEmail);
      setUserAvatar(gPhoto);
      setIsLoggedIn(true);
      setCurrentView('dashboard');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleResetPassword = async () => {
    if (!inputEmail.trim()) {
      alert("Please enter your email address first in the email field.");
      return;
    }
    try {
      await sendPasswordResetEmail(auth, inputEmail.trim());
      alert(`Password reset link sent to ${inputEmail}.\n\nIMPORTANT: Check your Inbox AND Spam/Junk folder!`);
    } catch (err) {
      alert(`Password reset link requested for ${inputEmail}.\n\nIMPORTANT: Check your Inbox AND Spam/Junk folder!`);
    }
  };

  const startWorkoutSession = (w) => {
    setSelectedWorkout(w);
    setWorkoutTimer(w.duration || 1200);
    setCompletedSets(1);
    setIsTimerRunning(true);
    setActiveModal('active_workout');
  };

  const toggleMealEaten = (id, kcal) => {
    setEatenMeals(prev => {
      const isDone = !prev[id];
      if (isDone) setCalories(c => c + kcal);
      else setCalories(c => Math.max(0, c - kcal));
      return { ...prev, [id]: isDone };
    });
  };

  const addPantryIngredient = () => {
    if (pantryInput.trim() && !pantryIngredients.includes(pantryInput.trim())) {
      setPantryIngredients([...pantryIngredients, pantryInput.trim()]);
      setPantryInput('');
    }
  };

  const generatePantryRecipe = () => {
    setGeneratedRecipe({
      name: `High-Protein ${pantryIngredients.join(' & ')} Bowl`,
      kcal: 380,
      protein: '28g',
      img: 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400'
    });
  };

  const applyPantryRecipe = () => {
    if (generatedRecipe) {
      setMeals(prev => [...prev, {
        id: `m_${Date.now()}`,
        name: generatedRecipe.name,
        kcal: generatedRecipe.kcal,
        type: 'Lunch',
        img: generatedRecipe.img
      }]);
      setCalories(c => c + generatedRecipe.kcal);
      setActiveModal(null);
      setGeneratedRecipe(null);
    }
  };

  const submitQuickLog = (e) => {
    e.preventDefault();
    if (logMealName && logMealKcal) {
      const kcalVal = parseInt(logMealKcal);
      setMeals(prev => [...prev, {
        id: `m_${Date.now()}`,
        name: logMealName,
        kcal: kcalVal,
        type: 'Quick Log',
        img: 'https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=400'
      }]);
      setCalories(c => c + kcalVal);
      setLogMealName('');
      setLogMealKcal('');
      setActiveModal(null);
    }
  };

  const submitNewWeight = (e) => {
    e.preventDefault();
    if (newWeightInput) {
      const wVal = parseFloat(newWeightInput);
      setUserWeight(wVal);
      setWeightHistory(prev => [{ date: 'Today', weight: wVal }, ...prev]);
      setNewWeightInput('');
      setActiveModal(null);
    }
  };

  const applyRegionalPlan = (planName, b, l, d) => {
    setMeals([
      { id: 'r1', name: b.name, kcal: b.kcal, type: 'Breakfast', img: b.img },
      { id: 'r2', name: l.name, kcal: l.kcal, type: 'Lunch', img: l.img },
      { id: 'r3', name: d.name, kcal: d.kcal, type: 'Dinner', img: d.img }
    ]);
    setCalories(b.kcal + l.kcal + d.kcal);
    setActiveModal(null);
  };

  const highStressYoga = [
    { id: 101, title: "Bhramari (Humming Bee Breath)", category: "Yoga", subtitle: "15 min • High Stress Calming", img: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800", reps: "10 Rounds", sets: 3, duration: 900, benefits: "Instantly calms agitated mind & lowers anxiety.", steps: ["Sit with eyes closed.", "Place index fingers on ear tragus.", "Inhale deeply.", "Exhale making humming bee sound ('Mmmmm')."] },
    { id: 102, title: "Sheetali (Cooling Breath)", category: "Yoga", subtitle: "10 min • Heat & Stress Release", img: "https://images.unsplash.com/photo-1552196563-55cd4e45efb3?w=800", reps: "15 Rounds", sets: 3, duration: 600, benefits: "Cools body temperature & calms heart rate.", steps: ["Roll tongue into a tube.", "Inhale cool air through tongue.", "Close mouth, exhale through nose."] },
    { id: 103, title: "Anulom Vilom (Alternate Nostril)", category: "Yoga", subtitle: "20 min • Mind Balance", img: "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800", reps: "15 Mins", sets: 4, duration: 1200, benefits: "Balances nervous system & lowers cortisol.", steps: ["Sit erect with eyes closed.", "Close right nostril with thumb, inhale left.", "Close left nostril, exhale right.", "Inhale right, close right, exhale left."] }
  ];

  const moderateStressYoga = [
    { id: 201, title: "Nadi Shodhana Pranayama", category: "Yoga", subtitle: "12 min • Harmony Flow", img: "https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800", reps: "12 Cycles", sets: 3, duration: 720, benefits: "Clears energy channels & improves focus.", steps: ["Sit in Vishnu Mudra.", "Alternate breathing with gentle pauses."] },
    { id: 202, title: "Balasana (Child's Pose Flow)", category: "Yoga", subtitle: "10 min • Restorative Stretch", img: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800", reps: "5 Mins Hold", sets: 2, duration: 600, benefits: "Stretches hips & lower back.", steps: ["Kneel on floor with big toes touching.", "Lower torso between thighs and extend arms."] },
    { id: 203, title: "Vrikshasana (Tree Pose Balance)", category: "Yoga", subtitle: "10 min • Focus & Balance", img: "https://images.unsplash.com/photo-1510894347713-fc3ed6fdf539?w=800", reps: "2 Mins Hold", sets: 3, duration: 600, benefits: "Enhances physical balance & leg strength.", steps: ["Shift weight onto left foot.", "Place right sole on inner left thigh.", "Hold palms in Namaste."] }
  ];

  const lowStressYoga = [
    { id: 301, title: "Kapalbhati (Skull-Shining)", category: "Yoga", subtitle: "15 min • Detox & Vitality", img: "https://images.unsplash.com/photo-1510894347713-fc3ed6fdf539?w=800", reps: "3 Rounds", sets: 3, duration: 900, benefits: "Detoxifies lungs & energizes core.", steps: ["Forcefully contract abdomen to pump air out.", "Passive inhalation between exhalations."] },
    { id: 302, title: "Surya Namaskar (Sun Flow)", category: "Yoga", subtitle: "15 min • Full Body Vitality", img: "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=800", reps: "12 Cycles", sets: 3, duration: 900, benefits: "Full body flexibility & posture alignment.", steps: ["Stand straight in Pranamasana.", "Inhale up, exhale down into forward bend."] },
    { id: 303, title: "Virabhadrasana (Warrior Flow)", category: "Yoga", subtitle: "20 min • Strength & Power", img: "https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=800", reps: "5 Breaths Hold", sets: 4, duration: 1200, benefits: "Builds leg strength & confidence.", steps: ["Step feet wide apart.", "Turn right foot out and bend right knee.", "Extend arms parallel to floor."] }
  ];

  const currentYogaSet = stressLevel >= 7 ? highStressYoga : stressLevel >= 4 ? moderateStressYoga : lowStressYoga;

  const filteredWorkouts = activeFilter === 'all' 
    ? [...workouts.filter(w => w.category !== 'Yoga'), ...currentYogaSet] 
    : activeFilter === 'Yoga'
    ? currentYogaSet
    : workouts.filter(w => w.category === activeFilter);

  const waterPercent = Math.min(100, Math.round((waterCount / 8) * 100));
  const caloriePercent = Math.min(100, Math.round((calories / 2000) * 100));
  const heightM = userHeight / 100;
  const bmi = (userWeight / (heightM * heightM)).toFixed(1);

  const formatTimer = (secs) => {
    const m = Math.floor(secs / 60);
    const s = secs % 60;
    return `${m}:${s < 10 ? '0' : ''}${s}`;
  };

  const user = {
    name: userName,
    email: userEmail,
    avatar: 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200',
    weight: userWeight,
    score: 78
  };

  // Auth View
  if (!isLoggedIn) {
    return (
      <div className="app-root" style={{ justifyContent: 'center', alignItems: 'center', minHeight: '100vh', padding: '20px' }}>
        <div className="glass-card" style={{ maxWidth: '420px', width: '100%', padding: '36px 28px', textAlign: 'center' }}>
          <div className="brand-icon" style={{ margin: '0 auto 16px auto', width: '56px', height: '56px' }}><Zap size={32} /></div>
          <h2 style={{ fontSize: '28px', marginBottom: '8px' }}>FitTrack AI</h2>
          <p style={{ color: 'var(--text-secondary)', marginBottom: '20px', fontSize: '14px' }}>
            {authMode === 'signin' ? 'Sign in to access FitTrack' : 'Create your FitTrack account'}
          </p>

          <div style={{ display: 'flex', gap: '8px', marginBottom: '24px', background: 'var(--bg-surface)', padding: '4px', borderRadius: '12px' }}>
            <button 
              className={`tab-btn ${authMode === 'signin' ? 'active' : ''}`}
              style={{ flex: 1, borderRadius: '8px', padding: '8px' }}
              onClick={() => setAuthMode('signin')}
            >
              Sign In
            </button>
            <button 
              className={`tab-btn ${authMode === 'signup' ? 'active' : ''}`}
              style={{ flex: 1, borderRadius: '8px', padding: '8px' }}
              onClick={() => setAuthMode('signup')}
            >
              Sign Up
            </button>
          </div>
          
          <form onSubmit={authMode === 'signin' ? handleSignIn : handleSignUp} style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {authMode === 'signup' && (
              <input 
                type="text" 
                value={inputName} 
                onChange={e => setInputName(e.target.value)} 
                placeholder="Full Name"
                style={{ width: '100%', padding: '12px 16px', borderRadius: '12px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff', outline: 'none' }}
                required 
              />
            )}
            <input 
              type="email" 
              value={inputEmail} 
              onChange={e => setInputEmail(e.target.value)} 
              placeholder="Email Address"
              style={{ width: '100%', padding: '12px 16px', borderRadius: '12px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff', outline: 'none' }}
              required 
            />
            <input 
              type="password" 
              value={inputPassword} 
              onChange={e => setInputPassword(e.target.value)} 
              placeholder="Password"
              style={{ width: '100%', padding: '12px 16px', borderRadius: '12px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff', outline: 'none' }}
              required 
            />

            {authMode === 'signin' && (
              <div style={{ textAlign: 'right', marginTop: '-6px' }}>
                <button 
                  type="button" 
                  onClick={handleResetPassword}
                  style={{ background: 'none', border: 'none', color: 'var(--primary)', fontSize: '12px', cursor: 'pointer', textDecoration: 'underline' }}
                >
                  Forgot Password?
                </button>
              </div>
            )}

            <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '14px', marginTop: '8px' }}>
              {authMode === 'signin' ? 'Sign In to FitTrack' : 'Create Account'}
            </button>
          </form>

          <div style={{ display: 'flex', alignItems: 'center', margin: '16px 0', color: 'var(--text-secondary)', fontSize: '12px' }}>
            <div style={{ flex: 1, height: '1px', background: 'var(--border)' }}></div>
            <span style={{ padding: '0 10px' }}>OR</span>
            <div style={{ flex: 1, height: '1px', background: 'var(--border)' }}></div>
          </div>

          <button 
            type="button" 
            className="btn-primary" 
            onClick={handleGoogleSignIn}
            style={{ width: '100%', justifyContent: 'center', padding: '12px', background: '#ffffff', color: '#1f2937', fontWeight: 600 }}
          >
            <svg width="18" height="18" viewBox="0 0 24 24" style={{ marginRight: '8px' }}>
              <path fill="#4285F4" d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"/>
              <path fill="#34A853" d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"/>
              <path fill="#FBBC05" d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.06H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.94l2.85-2.22.81-.63z"/>
              <path fill="#EA4335" d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.06l3.66 2.84c.87-2.6 3.3-4.52 6.16-4.52z"/>
            </svg>
            Sign in with Google
          </button>
          
          <div style={{ marginTop: '20px', fontSize: '13px', color: 'var(--text-secondary)' }}>
            User Account: {userEmail}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="app-root">
      {/* Sidebar Navigation */}
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon"><Zap size={24} /></div>
          <div className="brand-title">FitTrack</div>
        </div>

        <ul className="nav-menu">
          <li className={`nav-item ${currentView === 'dashboard' ? 'active' : ''}`} onClick={() => setCurrentView('dashboard')}>
            <Home size={20} /> <span>Dashboard</span>
          </li>
          <li className={`nav-item ${currentView === 'workout' ? 'active' : ''}`} onClick={() => setCurrentView('workout')}>
            <Dumbbell size={20} /> <span>Workouts</span>
          </li>
          <li className={`nav-item ${currentView === 'diet' ? 'active' : ''}`} onClick={() => setCurrentView('diet')}>
            <Utensils size={20} /> <span>Diet & Meals</span>
          </li>
          <li className={`nav-item ${currentView === 'water' ? 'active' : ''}`} onClick={() => setCurrentView('water')}>
            <Droplet size={20} /> <span>Water Tracker</span>
          </li>
          <li className={`nav-item ${currentView === 'progress' ? 'active' : ''}`} onClick={() => setCurrentView('progress')}>
            <ChartLine size={20} /> <span>Progress AI</span>
          </li>
          <li className={`nav-item ${currentView === 'profile' ? 'active' : ''}`} onClick={() => setCurrentView('profile')}>
            <User size={20} /> <span>Profile & Settings</span>
          </li>
        </ul>

        <div className="sidebar-user" onClick={() => setCurrentView('profile')}>
          <img src={user.avatar} alt="Avatar" className="avatar" />
          <div className="user-info">
            <div className="user-name">{user.name}</div>
            <div className="user-sub">Pro Member</div>
          </div>
        </div>
      </aside>

      {/* Main Content Area */}
      <main className="main-content">
        <header className="header-bar">
          <div>
            <div className="greeting-sub">Good Morning</div>
            <h1 className="user-greeting">Welcome, {user.name.split(' ')[0]} 👋</h1>
          </div>
          <div className="header-actions">
            <button className="icon-btn" onClick={() => setActiveModal('notifications')} title="Notifications"><Bell size={18} /></button>
            <button className="icon-btn" onClick={() => setCurrentView('profile')} title="Settings"><Settings size={18} /></button>
          </div>
        </header>

        {/* VIEW 1: DASHBOARD */}
        {currentView === 'dashboard' && (
          <div>
            <div className="grid-3" style={{ marginBottom: '24px' }}>
              <div className="glass-card score-card" style={{ cursor: 'pointer' }} onClick={() => setActiveModal('score_breakdown')}>
                <div style={{ fontWeight: 600, color: 'var(--text-secondary)' }}>FitTrack AI Score</div>
                <div className="ring-container">
                  <svg viewBox="0 0 160 160">
                    <defs>
                      <linearGradient id="scoreGrad" x1="0%" y1="0%" x2="100%" y2="100%">
                        <stop offset="0%" stopColor="#10B981" />
                        <stop offset="100%" stopColor="#06B6D4" />
                      </linearGradient>
                    </defs>
                    <circle className="ring-bg" cx="80" cy="80" r="70" />
                    <circle className="ring-progress" cx="80" cy="80" r="70" style={{ strokeDashoffset: 440 - (440 * user.score) / 100 }} />
                  </svg>
                  <div className="score-number">{user.score}</div>
                </div>
                <span className="badge-chip">Excellent • Click for Details</span>
              </div>

              <div className="glass-card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ color: 'var(--text-secondary)', marginBottom: '4px' }}>Diet Intake</div>
                  <h2>{calories} / 2,000 kcal</h2>
                </div>
                <div>
                  <div className="stat-header">
                    <span>Daily Goal</span>
                    <span>{caloriePercent}%</span>
                  </div>
                  <div className="progress-track">
                    <div className="progress-fill" style={{ width: `${caloriePercent}%` }}></div>
                  </div>
                </div>
                <button className="btn-primary" onClick={() => setActiveModal('quick_log')} style={{ marginTop: '16px' }}>
                  <Plus size={16} /> Log Meal
                </button>
              </div>

              <div className="glass-card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
                <div>
                  <div style={{ color: 'var(--text-secondary)', marginBottom: '4px' }}>Hydration</div>
                  <h2>{waterCount} / 8 Glasses</h2>
                </div>
                <div>
                  <div className="stat-header">
                    <span>Target</span>
                    <span>{waterPercent}%</span>
                  </div>
                  <div className="progress-track">
                    <div className="progress-fill" style={{ width: `${waterPercent}%`, background: 'linear-gradient(90deg, #06B6D4, #3B82F6)' }}></div>
                  </div>
                </div>
                <button className="btn-primary" onClick={() => { setWaterCount(w => Math.min(8, w + 1)); setCurrentView('water'); }} style={{ marginTop: '16px', background: 'linear-gradient(135deg, #06B6D4, #2563EB)' }}>
                  <Droplet size={16} /> Add Water
                </button>
              </div>
            </div>

            <div className="glass-card" style={{ marginBottom: '24px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px', background: 'linear-gradient(135deg, rgba(16, 185, 129, 0.1) 0%, rgba(6, 182, 212, 0.1) 100%)' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
                <div className="brand-icon" style={{ width: '48px', height: '48px' }}><Brain size={24} /></div>
                <div>
                  <h3 style={{ fontSize: '18px', margin: 0 }}>AI Stress &amp; Mood Mapper</h3>
                  <p style={{ color: 'var(--text-secondary)', fontSize: '13px', margin: '4px 0 0 0' }}>
                    Current Stress Level: <strong style={{ color: stressLevel >= 7 ? '#ef4444' : stressLevel >= 4 ? '#f59e0b' : '#10b981' }}>{stressLevel}/10</strong> ({userMood})
                  </p>
                </div>
              </div>
              <button className="btn-primary" onClick={() => setActiveModal('stress_mapper')}>
                <Activity size={16} /> Open Stress Mapper
              </button>
            </div>

            <div className="hero-card" style={{ marginBottom: '24px' }}>
              <div className="hero-content">
                <span style={{ background: 'rgba(16,185,129,0.3)', padding: '4px 12px', borderRadius: '20px', fontSize: '12px', fontWeight: 600, color: '#10B981' }}>
                  RECOMMENDED TODAY
                </span>
                <h2 style={{ fontSize: '28px', margin: '8px 0' }}>Morning Cardio Blitz</h2>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '16px' }}>20 min • 3 Sets • 250 kcal Burn</p>
                <button className="btn-primary" onClick={() => startWorkoutSession(workouts[0])}>
                  <Play size={16} /> Start Workout Now
                </button>
              </div>
            </div>
          </div>
        )}

        {/* VIEW 2: WORKOUTS */}
        {currentView === 'workout' && (
          <div>
            <div className="tab-row">
              {['all', 'Strength', 'Cardio', 'Yoga'].map(cat => (
                <button 
                  key={cat} 
                  className={`tab-btn ${activeFilter === cat ? 'active' : ''}`}
                  onClick={() => setActiveFilter(cat)}
                >
                  {cat.charAt(0).toUpperCase() + cat.slice(1)}
                </button>
              ))}
            </div>

            <div className="grid-3">
              {filteredWorkouts.map(w => (
                <div key={w.id} className="workout-card">
                  <img src={w.img} alt={w.title} className="workout-img" />
                  <div className="workout-body">
                    <span style={{ fontSize: '11px', fontWeight: 700, color: 'var(--primary)', textTransform: 'uppercase' }}>{w.category}</span>
                    <h4 style={{ margin: '4px 0 8px 0' }}>{w.title}</h4>
                    <p style={{ fontSize: '13px', color: 'var(--text-secondary)', marginBottom: '12px' }}>{w.subtitle} • {w.reps}</p>
                    <button className="btn-primary" onClick={() => startWorkoutSession(w)} style={{ width: '100%', justifyContent: 'center' }}>
                      <Play size={16} /> Start Session
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* VIEW 3: DIET & MEALS */}
        {currentView === 'diet' && (
          <div>
            <div className="tab-row" style={{ overflowX: 'auto', paddingBottom: '4px', marginBottom: '20px' }}>
              {Object.keys(dietPlans).map(planName => (
                <button 
                  key={planName}
                  className={`tab-btn ${activeDietPlan === planName ? 'active' : ''}`}
                  onClick={() => switchDietPlan(planName)}
                  style={{ whiteSpace: 'nowrap' }}
                >
                  {planName}
                </button>
              ))}
            </div>

            <div className="grid-2">
              <div className="glass-card">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
                  <div>
                    <h3>{activeDietPlan} Plan</h3>
                    <p style={{ color: 'var(--text-secondary)', fontSize: '14px' }}>Custom Rotational Nutrition Plan</p>
                  </div>
                  <button className="btn-primary" onClick={() => setActiveModal('quick_log')} style={{ padding: '8px 14px', fontSize: '13px' }}>
                    <Plus size={14} /> Add Custom
                  </button>
                </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                {meals.map(m => {
                  const isEaten = eatenMeals[m.id];
                  return (
                    <div key={m.id} style={{ display: 'flex', alignItems: 'center', gap: '14px', padding: '12px', borderBottom: '1px solid var(--border)' }}>
                      <img src={m.img} alt={m.name} style={{ width: '50px', height: '50px', borderRadius: '12px', objectFit: 'cover' }} />
                      <div style={{ flex: 1 }}>
                        <div style={{ fontWeight: 600, fontSize: '14px' }}>{m.name}</div>
                        <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>{m.type} • {m.kcal} kcal</div>
                      </div>
                      <button 
                        className="btn-primary" 
                        onClick={() => toggleMealEaten(m.id, m.kcal)}
                        style={{ padding: '6px 12px', fontSize: '12px', background: isEaten ? '#059669' : 'var(--bg-surface)', border: '1px solid var(--border)' }}
                      >
                        <Check size={14} /> {isEaten ? 'Eaten' : 'Log'}
                      </button>
                    </div>
                  );
                })}
              </div>
            </div>

            <div className="glass-card" style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <h3>Diet Tools</h3>
              <button className="btn-primary" onClick={() => setActiveModal('regional_diet')} style={{ background: 'var(--bg-surface)', border: '1px solid var(--border)', width: '100%', justifyContent: 'flex-start' }}>
                <Earth size={18} /> Regional Indian Diets
              </button>
              <button className="btn-primary" onClick={() => setActiveModal('pantry_generator')} style={{ background: 'var(--bg-surface)', border: '1px solid var(--border)', width: '100%', justifyContent: 'flex-start' }}>
                <Carrot size={18} /> Pantry Meal Generator
              </button>
              <button className="btn-primary" onClick={() => setActiveModal('quick_log')} style={{ background: 'var(--bg-surface)', border: '1px solid var(--border)', width: '100%', justifyContent: 'flex-start' }}>
                <Bolt size={18} /> Quick Calorie Log
              </button>
            </div>
          </div>
        </div>
      )}

        {/* VIEW 4: WATER TRACKER */}
        {currentView === 'water' && (
          <div className="glass-card water-interactive" style={{ maxWidth: '500px', margin: '0 auto' }}>
            <h2>Daily Hydration Goal</h2>
            <p style={{ color: 'var(--text-secondary)' }}>
              {8 - waterCount > 0 ? `Drink ${8 - waterCount} more glasses to reach your target` : 'Daily hydration target completed! 🎉'}
            </p>
            <div className="water-bottle">
              <div className="water-fill" style={{ height: `${waterPercent}%` }}></div>
            </div>
            <h1>{waterCount} / 8 Glasses</h1>
            <div style={{ display: 'flex', gap: '12px', flexWrap: 'wrap', justifyContent: 'center' }}>
              <button className="btn-primary" onClick={() => setWaterCount(w => Math.min(8, w + 1))} style={{ background: 'linear-gradient(135deg, #06B6D4, #2563EB)' }}>
                <Plus size={16} /> +1 Glass
              </button>
              <button className="btn-primary" onClick={() => setWaterCount(w => Math.min(8, w + 2))} style={{ background: 'linear-gradient(135deg, #06B6D4, #2563EB)' }}>
                <Plus size={16} /> +2 Glasses
              </button>
              <button className="btn-primary" onClick={() => setWaterCount(w => Math.max(0, w - 1))} style={{ background: 'var(--bg-surface)', border: '1px solid var(--border)' }}>
                -1 Glass
              </button>
              <button className="btn-primary" onClick={() => setWaterCount(0)} style={{ background: 'var(--bg-surface)', border: '1px solid var(--border)' }}>
                <RotateCcw size={16} /> Reset
              </button>
            </div>
          </div>
        )}

        {/* VIEW 5: PROGRESS AI */}
        {currentView === 'progress' && (
          <div>
            <div className="grid-2" style={{ marginBottom: '24px' }}>
              <div className="glass-card">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <h3>Weight & BMI Stats</h3>
                  <button className="btn-primary" onClick={() => setActiveModal('add_weight')} style={{ padding: '6px 12px', fontSize: '12px' }}>
                    <Plus size={14} /> Log Weight
                  </button>
                </div>
                <div style={{ fontSize: '36px', fontWeight: 800, margin: '12px 0', color: 'var(--primary)' }}>{userWeight} kg</div>
                <p style={{ color: 'var(--text-secondary)' }}>BMI: {bmi} ({bmi < 25 ? 'Healthy Weight' : 'Overweight'})</p>

                <div style={{ marginTop: '20px' }}>
                  <h4 style={{ fontSize: '14px', marginBottom: '8px' }}>Recent Weight History</h4>
                  {weightHistory.map((h, i) => (
                    <div key={i} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', borderBottom: '1px solid var(--border)', fontSize: '13px' }}>
                      <span>{h.date}</span>
                      <span style={{ fontWeight: 600 }}>{h.weight} kg</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="glass-card">
                <h3>AI Goal Predictor</h3>
                <p style={{ color: 'var(--text-secondary)', margin: '8px 0' }}>Target Goal: {goalWeight} kg</p>
                <div style={{ fontSize: '24px', fontWeight: 700, color: 'var(--secondary)', margin: '12px 0' }}>
                  Estimated Date: Sept 24, 2026
                </div>
                <p style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
                  Based on your current weight loss rate of 0.4 kg / week, you are on track to achieve your target in 8 weeks!
                </p>
              </div>
            </div>
          </div>
        )}

        {/* VIEW 6: PROFILE & SETTINGS */}
        {currentView === 'profile' && (
          <div className="glass-card" style={{ maxWidth: '600px', margin: '0 auto' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '20px', marginBottom: '24px' }}>
              <img src={user.avatar} alt="Avatar" className="avatar" style={{ width: '80px', height: '80px' }} />
              <div style={{ flex: 1 }}>
                <h2>{user.name}</h2>
                <p style={{ color: 'var(--text-secondary)' }}>{user.email}</p>
              </div>
              <button className="btn-primary" onClick={() => setActiveModal('edit_profile')} style={{ padding: '8px 14px', fontSize: '13px' }}>
                Edit Profile
              </button>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
              <div className="stat-header" style={{ alignItems: 'center' }}>
                <span>Dark Mode</span>
                <input 
                  type="checkbox" 
                  checked={darkMode} 
                  onChange={e => { setDarkMode(e.target.checked); localStorage.setItem('darkMode', e.target.checked); }} 
                />
              </div>
              <div className="stat-header" style={{ alignItems: 'center' }}>
                <span>Push Notifications</span>
                <input 
                  type="checkbox" 
                  checked={notificationsEnabled} 
                  onChange={e => setNotificationsEnabled(e.target.checked)} 
                />
              </div>
              <button className="btn-primary" onClick={handleLogout} style={{ background: '#E53935', marginTop: '20px', width: '100%', justifyContent: 'center' }}>
                <LogOut size={16} /> Log Out
              </button>
            </div>
          </div>
        )}
      </main>

      {/* MOBILE BOTTOM NAVIGATION */}
      <nav className="bottom-nav">
        <div className={`bottom-nav-item ${currentView === 'dashboard' ? 'active' : ''}`} onClick={() => setCurrentView('dashboard')}>
          <Home size={18} /> <span>Home</span>
        </div>
        <div className={`bottom-nav-item ${currentView === 'workout' ? 'active' : ''}`} onClick={() => setCurrentView('workout')}>
          <Dumbbell size={18} /> <span>Workout</span>
        </div>
        <div className={`bottom-nav-item ${currentView === 'diet' ? 'active' : ''}`} onClick={() => setCurrentView('diet')}>
          <Utensils size={18} /> <span>Diet</span>
        </div>
        <div className={`bottom-nav-item ${currentView === 'water' ? 'active' : ''}`} onClick={() => setCurrentView('water')}>
          <Droplet size={18} /> <span>Water</span>
        </div>
        <div className={`bottom-nav-item ${currentView === 'profile' ? 'active' : ''}`} onClick={() => setCurrentView('profile')}>
          <User size={18} /> <span>Profile</span>
        </div>
      </nav>

      {/* MODAL OVERLAYS */}
      {activeModal && (
        <div style={{ position: 'fixed', inset: 0, background: 'rgba(0,0,0,0.75)', backdropFilter: 'blur(8px)', zIndex: 2000, display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '20px' }}>
          <div className="glass-card" style={{ maxWidth: '500px', width: '100%', position: 'relative', maxHeight: '90vh', overflowY: 'auto' }}>
            <button onClick={() => setActiveModal(null)} style={{ position: 'absolute', top: '16px', right: '16px', background: 'none', border: 'none', color: '#fff', cursor: 'pointer' }}>
              <X size={20} />
            </button>

            {/* MODAL 1: ACTIVE WORKOUT */}
            {activeModal === 'active_workout' && selectedWorkout && (
              <div style={{ textAlign: 'center' }}>
                <h3 style={{ fontSize: '22px', marginBottom: '8px' }}>{selectedWorkout.title}</h3>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '16px' }}>Set {completedSets} of {selectedWorkout.sets} • {selectedWorkout.reps}</p>
                <img src={selectedWorkout.img} alt="Workout" style={{ width: '100%', height: '180px', objectFit: 'cover', borderRadius: '16px', marginBottom: '16px' }} />
                
                {selectedWorkout.steps && selectedWorkout.steps.length > 0 && (
                  <div style={{ textAlign: 'left', marginBottom: '20px', padding: '12px', background: 'var(--bg-surface)', borderRadius: '12px', border: '1px solid var(--border)' }}>
                    <h4 style={{ fontSize: '14px', marginBottom: '8px', color: 'var(--primary)' }}>How to perform:</h4>
                    <ol style={{ fontSize: '13px', color: 'var(--text-secondary)', paddingLeft: '20px', margin: 0, display: 'flex', flexDirection: 'column', gap: '6px' }}>
                      {selectedWorkout.steps.map((step, idx) => (
                        <li key={idx}>{step}</li>
                      ))}
                    </ol>
                  </div>
                )}

                <div style={{ fontSize: '48px', fontWeight: 800, fontFamily: 'Outfit', color: 'var(--primary)', marginBottom: '20px' }}>
                  {formatTimer(workoutTimer)}
                </div>

                <div style={{ display: 'flex', gap: '12px', justifyContent: 'center' }}>
                  <button className="btn-primary" onClick={() => setIsTimerRunning(!isTimerRunning)}>
                    {isTimerRunning ? <Pause size={16} /> : <Play size={16} />}
                    {isTimerRunning ? 'Pause' : 'Resume'}
                  </button>
                  <button className="btn-primary" onClick={() => setCompletedSets(s => Math.min(selectedWorkout.sets, s + 1))}>
                    Next Set
                  </button>
                  <button className="btn-primary" onClick={() => setActiveModal(null)} style={{ background: '#059669' }}>
                    Finish Workout
                  </button>
                </div>
              </div>
            )}

            {/* MODAL 2: REGIONAL DIET */}
            {activeModal === 'regional_diet' && (
              <div>
                <h3>Regional Indian Diet Plans</h3>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '16px', fontSize: '14px' }}>Choose a regional nutrition plan to apply to your daily diet:</p>
                
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                  <div style={{ padding: '12px', background: 'var(--bg-surface)', borderRadius: '12px', border: '1px solid var(--border)' }}>
                    <h4 style={{ color: 'var(--primary)' }}>South Indian Plan</h4>
                    <p style={{ fontSize: '12px', color: 'var(--text-secondary)', margin: '4px 0' }}>Breakfast: Oats Idli & Sambhar | Lunch: Brown Rice Rasam | Dinner: Ragi Dosa</p>
                    <button className="btn-primary" onClick={() => applyRegionalPlan('South Indian', { name: 'Oats Idli with Sambhar', kcal: 280, img: 'https://images.unsplash.com/photo-1589301760014-d929f3979dbc?w=400' }, { name: 'Brown Rice & Rasam', kcal: 420, img: 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400' }, { name: 'Ragi Dosa', kcal: 310, img: 'https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=800' })} style={{ padding: '6px 12px', fontSize: '12px', marginTop: '8px' }}>Apply Plan</button>
                  </div>

                  <div style={{ padding: '12px', background: 'var(--bg-surface)', borderRadius: '12px', border: '1px solid var(--border)' }}>
                    <h4 style={{ color: 'var(--secondary)' }}>North Indian Plan</h4>
                    <p style={{ fontSize: '12px', color: 'var(--text-secondary)', margin: '4px 0' }}>Breakfast: Stuffed Paratha | Lunch: Paneer Tikka Roti | Dinner: Dal Tadka Rice</p>
                    <button className="btn-primary" onClick={() => applyRegionalPlan('North Indian', { name: 'Stuffed Paratha', kcal: 350, img: 'https://images.unsplash.com/photo-1604152135912-04a002e75696?w=400' }, { name: 'Paneer Tikka Roti', kcal: 450, img: 'https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=400' }, { name: 'Dal Tadka & Rice', kcal: 380, img: 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=400' })} style={{ padding: '6px 12px', fontSize: '12px', marginTop: '8px' }}>Apply Plan</button>
                  </div>
                </div>
              </div>
            )}

            {/* MODAL 3: PANTRY GENERATOR */}
            {activeModal === 'pantry_generator' && (
              <div>
                <h3>Pantry Meal Generator</h3>
                <p style={{ color: 'var(--text-secondary)', marginBottom: '16px', fontSize: '14px' }}>Add available ingredients from your kitchen:</p>

                <div style={{ display: 'flex', gap: '8px', marginBottom: '16px' }}>
                  <input 
                    type="text" 
                    value={pantryInput} 
                    onChange={e => setPantryInput(e.target.value)} 
                    placeholder="e.g. Eggs, Tomatoes, Rice"
                    style={{ flex: 1, padding: '10px 14px', borderRadius: '10px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                  />
                  <button className="btn-primary" onClick={addPantryIngredient}>Add</button>
                </div>

                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginBottom: '20px' }}>
                  {pantryIngredients.map((tag, i) => (
                    <span key={i} className="badge-chip">{tag}</span>
                  ))}
                </div>

                <button className="btn-primary" onClick={generatePantryRecipe} style={{ width: '100%', justifyContent: 'center', marginBottom: '16px' }}>
                  <Sparkles size={16} /> Generate Recipe
                </button>

                {generatedRecipe && (
                  <div style={{ padding: '14px', background: 'var(--bg-surface)', borderRadius: '12px', border: '1px solid var(--primary)' }}>
                    <h4 style={{ color: 'var(--primary)' }}>{generatedRecipe.name}</h4>
                    <p style={{ fontSize: '13px', color: 'var(--text-secondary)', margin: '4px 0' }}>{generatedRecipe.kcal} kcal • {generatedRecipe.protein} Protein</p>
                    <button className="btn-primary" onClick={applyPantryRecipe} style={{ marginTop: '10px', width: '100%', justifyContent: 'center' }}>
                      Add Recipe to Daily Diet
                    </button>
                  </div>
                )}
              </div>
            )}

            {/* MODAL 4: QUICK LOG */}
            {activeModal === 'quick_log' && (
              <div>
                <h3>Quick Calorie Log</h3>
                <form onSubmit={submitQuickLog} style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginTop: '16px' }}>
                  <input 
                    type="text" 
                    value={logMealName} 
                    onChange={e => setLogMealName(e.target.value)} 
                    placeholder="Meal Name (e.g. Protein Shake)"
                    style={{ padding: '12px 14px', borderRadius: '10px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                    required 
                  />
                  <input 
                    type="number" 
                    value={logMealKcal} 
                    onChange={e => setLogMealKcal(e.target.value)} 
                    placeholder="Calories (kcal)"
                    style={{ padding: '12px 14px', borderRadius: '10px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                    required 
                  />
                  <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '12px' }}>
                    Save Calorie Entry
                  </button>
                </form>
              </div>
            )}

            {/* MODAL 5: ADD WEIGHT */}
            {activeModal === 'add_weight' && (
              <div>
                <h3>Log Weight Entry</h3>
                <form onSubmit={submitNewWeight} style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginTop: '16px' }}>
                  <input 
                    type="number" 
                    step="0.1"
                    value={newWeightInput} 
                    onChange={e => setNewWeightInput(e.target.value)} 
                    placeholder="Current Weight in kg (e.g. 68.2)"
                    style={{ padding: '12px 14px', borderRadius: '10px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                    required 
                  />
                  <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', padding: '12px' }}>
                    Save Weight Entry
                  </button>
                </form>
              </div>
            )}

            {/* MODAL 6: EDIT PROFILE */}
            {activeModal === 'edit_profile' && (
              <div>
                <h3>Edit Profile Details</h3>
                <form onSubmit={async (e) => { 
                  e.preventDefault(); 
                  localStorage.setItem('userName', userName);
                  localStorage.setItem('userHeight', userHeight);
                  localStorage.setItem('goalWeight', goalWeight);
                  localStorage.setItem('currentWeight', userWeight);
                  localStorage.setItem('profileImage', userAvatar);
                  if (auth.currentUser) {
                    try {
                      await updateDoc(doc(db, "users", auth.currentUser.uid), {
                        name: userName,
                        userHeight: userHeight,
                        currentWeight: userWeight,
                        profileImage: userAvatar
                      });
                    } catch (err) {
                      console.warn("Firestore updateDoc error:", err);
                    }
                  }
                  setActiveModal(null); 
                }} style={{ display: 'flex', flexDirection: 'column', gap: '14px', marginTop: '16px' }}>
                  
                  <div style={{ textAlign: 'center', marginBottom: '12px' }}>
                    <img src={userAvatar} alt="Profile" style={{ width: '84px', height: '84px', borderRadius: '50%', objectFit: 'cover', border: '3px solid var(--primary)', marginBottom: '12px', boxShadow: '0 4px 12px rgba(16, 185, 129, 0.3)' }} />
                    
                    <div style={{ display: 'flex', gap: '8px', justifyContent: 'center', alignItems: 'center', flexWrap: 'wrap', marginBottom: '10px' }}>
                      <button type="button" className="btn-primary" onClick={() => setUserAvatar('https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200')} style={{ padding: '6px 12px', fontSize: '11px', background: userAvatar.includes('1535713875002') ? 'var(--primary)' : 'var(--bg-surface)', border: '1px solid var(--border)' }}>Avatar 1</button>
                      <button type="button" className="btn-primary" onClick={() => setUserAvatar('https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200')} style={{ padding: '6px 12px', fontSize: '11px', background: userAvatar.includes('1534528741775') ? 'var(--primary)' : 'var(--bg-surface)', border: '1px solid var(--border)' }}>Avatar 2</button>
                      <button type="button" className="btn-primary" onClick={() => setUserAvatar('https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=200')} style={{ padding: '6px 12px', fontSize: '11px', background: userAvatar.includes('1570295999919') ? 'var(--primary)' : 'var(--bg-surface)', border: '1px solid var(--border)' }}>Avatar 3</button>
                    </div>

                    <label style={{ display: 'inline-flex', alignItems: 'center', gap: '6px', cursor: 'pointer', padding: '6px 14px', borderRadius: '8px', background: 'var(--bg-surface)', border: '1px solid var(--border)', fontSize: '12px', color: 'var(--text-secondary)' }}>
                      📷 Upload Local Photo
                      <input 
                        type="file" 
                        accept="image/*" 
                        onChange={(e) => {
                          const file = e.target.files[0];
                          if (file) {
                            const reader = new FileReader();
                            reader.onloadend = () => setUserAvatar(reader.result);
                            reader.readAsDataURL(file);
                          }
                        }} 
                        style={{ display: 'none' }} 
                      />
                    </label>
                  </div>

                  <div>
                    <label style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Full Name</label>
                    <input 
                      type="text" 
                      value={userName} 
                      onChange={e => setUserName(e.target.value)} 
                      style={{ width: '100%', padding: '10px', borderRadius: '8px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                    />
                  </div>
                  <div>
                    <label style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Height (cm)</label>
                    <input 
                      type="number" 
                      value={userHeight} 
                      onChange={e => setUserHeight(parseFloat(e.target.value) || 170)} 
                      style={{ width: '100%', padding: '10px', borderRadius: '8px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                    />
                  </div>
                  <div>
                    <label style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>Goal Weight (kg)</label>
                    <input 
                      type="number" 
                      value={goalWeight} 
                      onChange={e => setGoalWeight(parseFloat(e.target.value) || 65)} 
                      style={{ width: '100%', padding: '10px', borderRadius: '8px', background: 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff' }}
                    />
                  </div>
                  <button type="submit" className="btn-primary" style={{ width: '100%', justifyContent: 'center', marginTop: '8px' }}>
                    Save Profile Changes
                  </button>
                </form>
              </div>
            )}

            {/* MODAL 7: NOTIFICATIONS */}
            {activeModal === 'notifications' && (
              <div>
                <h3>Notifications</h3>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '12px', marginTop: '16px' }}>
                  <div style={{ padding: '10px', background: 'var(--bg-surface)', borderRadius: '10px', fontSize: '13px' }}>
                    💧 <strong>Hydration Alert:</strong> You are 3 glasses away from your 8-glass goal.
                  </div>
                  <div style={{ padding: '10px', background: 'var(--bg-surface)', borderRadius: '10px', fontSize: '13px' }}>
                    🔥 <strong>Workout Streak:</strong> 4 day workout streak maintained!
                  </div>
                </div>
              </div>
            )}

            {/* MODAL 8: SCORE BREAKDOWN */}
            {activeModal === 'score_breakdown' && (
              <div>
                <h3>FitTrack AI Score Breakdown</h3>
                <div style={{ fontSize: '36px', fontWeight: 800, color: 'var(--primary)', margin: '12px 0', textAlign: 'center' }}>
                  78 / 100
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', fontSize: '13px' }}>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>Workout Consistency:</span> <strong>35 / 40 pts</strong>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>Diet & Nutrition Balance:</span> <strong>24 / 30 pts</strong>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>Water Hydration Target:</span> <strong>12 / 15 pts</strong>
                  </div>
                  <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                    <span>Rest & Mind Balance:</span> <strong>7 / 15 pts</strong>
                  </div>
                </div>
              </div>
            )}

            {/* MODAL 9: STRESS MAPPER */}
            {activeModal === 'stress_mapper' && (
              <div>
                <h3>AI Stress &amp; Mood Mapper</h3>
                <p style={{ color: 'var(--text-secondary)', fontSize: '13px', marginBottom: '16px' }}>
                  Track your mental wellness and get personalized Pranayama &amp; Breathwork routines.
                </p>

                <div style={{ textAlign: 'center', marginBottom: '12px' }}>
                  <img src="https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=800" alt="Mindfulness" style={{ width: '100%', height: '140px', objectFit: 'cover', borderRadius: '14px', marginBottom: '16px' }} />

                  <div style={{ fontSize: '13px', fontWeight: 600, color: 'var(--text-secondary)', marginBottom: '8px' }}>How are you feeling today?</div>
                  <div style={{ display: 'flex', gap: '6px', justifyContent: 'center', marginBottom: '20px', flexWrap: 'wrap' }}>
                    {['Terrible', 'Bad', 'Okay', 'Good', 'Great'].map((m, i) => {
                      const icons = ['😫', '😟', '😐', '🙂', '😁'];
                      const isSelected = userMood === m;
                      return (
                        <button 
                          key={m}
                          type="button"
                          onClick={() => { setUserMood(m); localStorage.setItem('userMood', m); }}
                          style={{ padding: '8px 12px', borderRadius: '10px', background: isSelected ? 'var(--primary)' : 'var(--bg-surface)', border: '1px solid var(--border)', color: '#fff', fontSize: '13px', cursor: 'pointer', display: 'flex', alignItems: 'center', gap: '4px' }}
                        >
                          <span>{icons[i]}</span> <span>{m}</span>
                        </button>
                      );
                    })}
                  </div>

                  <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '8px' }}>
                    <span style={{ fontSize: '14px', fontWeight: 600 }}>Stress Level:</span>
                    <span style={{ fontSize: '18px', fontWeight: 800, color: stressLevel >= 7 ? '#ef4444' : stressLevel >= 4 ? '#f59e0b' : '#10b981' }}>{stressLevel} / 10</span>
                  </div>

                  <input 
                    type="range" 
                    min="1" 
                    max="10" 
                    value={stressLevel} 
                    onChange={e => {
                      const val = parseInt(e.target.value);
                      setStressLevel(val);
                      localStorage.setItem('userStress', val);
                    }}
                    style={{ width: '100%', accentColor: 'var(--primary)', marginBottom: '16px' }}
                  />

                  <div style={{ padding: '14px', borderRadius: '12px', background: 'var(--bg-surface)', border: '1px solid var(--border)', textAlign: 'left', marginBottom: '20px' }}>
                    <div style={{ fontSize: '13px', fontWeight: 700, color: 'var(--primary)', marginBottom: '4px', display: 'flex', alignItems: 'center', gap: '6px' }}>
                      <Brain size={16} /> Smart Recommendation
                    </div>
                    <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
                      {stressLevel >= 7 
                        ? "High stress detected. Recommended: 15 min Anulom Vilom & Deep Breathing to lower cortisol."
                        : stressLevel >= 4 
                        ? "Moderate stress level. Recommended: 10 min Nadi Shodhana & Gentle Yoga flow."
                        : "Low stress! Great energy state for Kapalbhati & Surya Namaskar Flow."}
                    </div>
                  </div>

                  <button 
                    className="btn-primary" 
                    onClick={() => {
                      setActiveModal(null);
                      setCurrentView('workout');
                      setActiveFilter('Yoga');
                    }}
                    style={{ width: '100%', justifyContent: 'center', padding: '14px' }}
                  >
                    <Activity size={18} /> Start Tailored Yoga &amp; Pranayama Session
                  </button>
                </div>
              </div>
            )}

          </div>
        </div>
      )}
    </div>
  );
}
