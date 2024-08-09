# Dummy Data for Drugs Table
```sql
-- Insert non-expired drugs with descriptions and tags
INSERT INTO Drugs (drug_id, drug_name, manufacturer, expiry_date, quantity, price, description, tags) VALUES
(1001, 'Aspirin', 'PharmaCorp', '2025-12-31', 100, 5.99, 'Pain reliever and anti-inflammatory', 'pain,fever,inflammation'),
(1002, 'Ibuprofen', 'HealthPlus', '2026-11-30', 200, 10.99, 'Anti-inflammatory medication used to reduce fever and treat pain or inflammation', 'pain,fever,inflammation'),
(1003, 'Paracetamol', 'MediLife', '2025-10-15', 150, 3.99, 'Pain reliever and a fever reducer', 'pain,fever'),
(1004, 'Amoxicillin', 'Antibiotix', '2025-09-20', 75, 15.49, 'Antibiotic used to treat various bacterial infections', 'infection,antibiotic,bacteria'),
(1005, 'Ciprofloxacin', 'BioPharma', '2026-01-25', 80, 12.89, 'Antibiotic used to treat bacterial infections', 'infection,antibiotic,bacteria'),
(1006, 'Lisinopril', 'HeartCare', '2025-08-30', 60, 8.75, 'Medication used to treat high blood pressure and heart failure', 'blood pressure,heart,hypertension'),
(1007, 'Metformin', 'Diabeat', '2025-07-14', 90, 4.50, 'Medication used to treat type 2 diabetes', 'diabetes,blood sugar'),
(1008, 'Omeprazole', 'StomachEase', '2025-11-25', 120, 7.20, 'Medication used to treat gastroesophageal reflux disease (GERD)', 'acid reflux,GERD,stomach'),
(1009, 'Atorvastatin', 'CholestrolFix', '2025-12-01', 110, 9.30, 'Medication used to lower cholesterol and triglyceride levels', 'cholesterol,heart,lipid'),
(1010, 'Levothyroxine', 'ThyroMed', '2026-02-14', 100, 6.40, 'Medication used to treat hypothyroidism', 'thyroid,hormone'),
(1011, 'Amlodipine', 'CardioSafe', '2025-06-20', 95, 5.10, 'Medication used to treat high blood pressure and coronary artery disease', 'blood pressure,heart,hypertension'),
(1012, 'Simvastatin', 'LipControl', '2026-03-10', 85, 6.90, 'Medication used to control high cholesterol', 'cholesterol,heart,lipid'),
(1013, 'Clopidogrel', 'BloodThinner', '2025-08-22', 70, 11.00, 'Medication used to prevent blood clots', 'blood clots,heart'),
(1014, 'Losartan', 'HyperTensionRelief', '2025-09-14', 65, 7.75, 'Medication used to treat high blood pressure', 'blood pressure,heart,hypertension'),
(1015, 'Gabapentin', 'NerveRelief', '2025-10-05', 130, 14.20, 'Medication used to treat nerve pain and seizures', 'nerve pain,seizures,neuropathy'),
(1016, 'ExpiredDrug1', 'ExpiredMeds', '2023-12-31', 50, 5.50, 'Expired pain reliever', 'expired,pain relief'),
(1017, 'ExpiredDrug2', 'OldPharma', '2022-11-30', 40, 6.60, 'Expired anti-inflammatory medication', 'expired,anti-inflammatory'),
(1018, 'ExpiredDrug3', 'PastMeds', '2021-10-15', 30, 7.70, 'Expired fever reducer', 'expired,fever reducer'),
(1019, 'ExpiredDrug4', 'OutdatedPharma', '2022-09-20', 25, 8.80, 'Expired antibiotic', 'expired,antibiotic'),
(1020, 'ExpiredDrug5', 'OldStock', '2023-01-25', 20, 9.90, 'Expired heart medication', 'expired,heart'),
(1021, 'Doxycycline', 'Antibiotix', '2025-12-31', 100, 13.45, 'Antibiotic used to treat bacterial infections', 'infection,antibiotic,bacteria'),
(1022, 'Metoprolol', 'HeartCare', '2025-11-30', 200, 10.75, 'Medication used to treat high blood pressure and chest pain', 'blood pressure,heart,angina'),
(1023, 'Albuterol', 'BreathEasy', '2026-10-15', 150, 12.00, 'Medication used to treat breathing problems such as asthma', 'asthma,breathing,bronchodilator'),
(1024, 'Pantoprazole', 'GastroCare', '2025-09-20', 75, 14.55, 'Medication used to treat stomach and esophagus problems', 'acid reflux,GERD,stomach'),
(1025, 'Zolpidem', 'SleepWell', '2025-01-25', 80, 8.25, 'Medication used to treat insomnia', 'insomnia,sleep aid'),
(1026, 'Hydrochlorothiazide', 'DiureticCo', '2025-08-15', 15, 5.60, 'Medication used to treat high blood pressure and fluid retention', 'blood pressure,diuretic'),
(1027, 'Furosemide', 'WaterPill', '2025-07-10', 10, 4.50, 'Diuretic used to reduce swelling and fluid retention', 'diuretic,fluid retention'),
(1028, 'Warfarin', 'AntiCoag', '2026-05-30', 18, 12.30, 'Anticoagulant used to prevent blood clots', 'anticoagulant,blood clots'),
(1029, 'Hydrocodone', 'PainRelief', '2025-09-05', 8, 15.00, 'Pain relief medication for severe pain', 'pain relief,opioid'),
(1030, 'Lorazepam', 'CalmMeds', '2025-11-20', 12, 9.80, 'Medication used to treat anxiety disorders', 'anxiety,calm');

SELECT * FROM Drugs;
```
