package com.example.data.seed

import com.example.data.model.EmergencyIncidentEntity
import com.example.data.model.FirstAidItemEntity
import com.example.data.model.SupplyRequisitionEntity
import com.example.data.model.TrafficBoothEntity

object InitialData {

  fun getInitialBooths(): List<TrafficBoothEntity> = listOf(
    TrafficBoothEntity(
      id = 1,
      boothCode = "TB-01",
      name = "Benz Circle Traffic Booth",
      junctionName = "Benz Circle (NH16 & MG Road)",
      area = "East Zone, Vijayawada",
      latitude = 16.4975,
      longitude = 80.6540,
      officerName = "SI K. Srinivasa Rao",
      officerPhone = "+91 94407 96201",
      officerBadge = "AP-VJA-1041",
      equipmentStatus = "EMERGENCY_ACTIVE",
      lastInspection = "Today, 07:15 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 2,
      boothCode = "TB-02",
      name = "Police Control Room Junction",
      junctionName = "MG Road & Old Bus Stand",
      area = "Central Zone, Vijayawada",
      latitude = 16.5108,
      longitude = 80.6272,
      officerName = "ASI M. Venkateswarlu",
      officerPhone = "+91 94407 96202",
      officerBadge = "AP-VJA-1188",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:45 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 3,
      boothCode = "TB-03",
      name = "Pandit Nehru Bus Station (PNBS)",
      junctionName = "Krishna River Road & Varadhi Approach",
      area = "South Zone, Vijayawada",
      latitude = 16.5140,
      longitude = 80.6190,
      officerName = "HC P. Ramesh",
      officerPhone = "+91 94407 96203",
      officerBadge = "AP-VJA-2415",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 07:00 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 4,
      boothCode = "TB-04",
      name = "Ramavarappadu Ring",
      junctionName = "Eluru Road & NH16 Ring Junction",
      area = "North-East Zone, Vijayawada",
      latitude = 16.5284,
      longitude = 80.6698,
      officerName = "SI D. Anjaneyulu",
      officerPhone = "+91 94407 96204",
      officerBadge = "AP-VJA-1092",
      equipmentStatus = "LOW_STOCK",
      lastInspection = "Yesterday, 05:30 PM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 5,
      boothCode = "TB-05",
      name = "Kanaka Durga Varadhi South",
      junctionName = "NH16 Varadhi Bridge & Tadepalli Toll",
      area = "South Corridor, Vijayawada",
      latitude = 16.5050,
      longitude = 80.6130,
      officerName = "ASI Ch. Ramana",
      officerPhone = "+91 94407 96205",
      officerBadge = "AP-VJA-1205",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:15 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 6,
      boothCode = "TB-06",
      name = "Bhavanipuram Junction",
      junctionName = "NH65 Hyderabad Road & Gollapudi Bypass",
      area = "West Zone, Vijayawada",
      latitude = 16.5312,
      longitude = 80.5985,
      officerName = "HC K. Appa Rao",
      officerPhone = "+91 94407 96206",
      officerBadge = "AP-VJA-2311",
      equipmentStatus = "EXPIRED_ALERT",
      lastInspection = "Yesterday, 04:00 PM",
      cctvConnected = false,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 7,
      boothCode = "TB-07",
      name = "Governorpet / Alankar Signal",
      junctionName = "Arundalpet & Gopalareddy Road",
      area = "Commercial Hub, Vijayawada",
      latitude = 16.5125,
      longitude = 80.6350,
      officerName = "SI T. Sivaram",
      officerPhone = "+91 94407 96207",
      officerBadge = "AP-VJA-1077",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:50 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 8,
      boothCode = "TB-08",
      name = "Auto Nagar Gate Junction",
      junctionName = "100-Ft Road & Industrial Corridor",
      area = "East Industrial, Vijayawada",
      latitude = 16.4950,
      longitude = 80.6780,
      officerName = "ASI G. Nageswara Rao",
      officerPhone = "+91 94407 96208",
      officerBadge = "AP-VJA-1150",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 07:10 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 9,
      boothCode = "TB-09",
      name = "Gannavaram Airport Junction",
      junctionName = "Airport Flyover & NH16",
      area = "Outer Ring East, Vijayawada",
      latitude = 16.5390,
      longitude = 80.7850,
      officerName = "SI V. Prasad",
      officerPhone = "+91 94407 96209",
      officerBadge = "AP-VJA-1033",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:00 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 10,
      boothCode = "TB-10",
      name = "Gollapudi Center",
      junctionName = "One Town & Ibrahimpatnam Highway",
      area = "Outer West Corridor, Vijayawada",
      latitude = 16.5450,
      longitude = 80.5820,
      officerName = "HC S. Murali Krishna",
      officerPhone = "+91 94407 96210",
      officerBadge = "AP-VJA-2290",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:20 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 11,
      boothCode = "TB-11",
      name = "Gunadala Center Signal",
      junctionName = "Eluru Road & Gunadala Shrine Arch",
      area = "North-East Corridor, Vijayawada",
      latitude = 16.5180,
      longitude = 80.6620,
      officerName = "SI P. Satyanarayana",
      officerPhone = "+91 94407 96211",
      officerBadge = "AP-VJA-1088",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:40 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 12,
      boothCode = "TB-12",
      name = "Machavaram Down Junction",
      junctionName = "BRTS Corridor & Siddhartha College Road",
      area = "Central East Zone, Vijayawada",
      latitude = 16.5170,
      longitude = 80.6480,
      officerName = "ASI B. Suresh Kumar",
      officerPhone = "+91 94407 96212",
      officerBadge = "AP-VJA-1219",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 07:05 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 13,
      boothCode = "TB-13",
      name = "One Town KR Market Junction",
      junctionName = "Kaleswara Rao Market & Main Bazaar",
      area = "Heritage Old Town, Vijayawada",
      latitude = 16.5210,
      longitude = 80.6090,
      officerName = "HC Y. Subrahmanyam",
      officerPhone = "+91 94407 96213",
      officerBadge = "AP-VJA-2344",
      equipmentStatus = "LOW_STOCK",
      lastInspection = "Yesterday, 06:10 PM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 14,
      boothCode = "TB-14",
      name = "NTR Health University Circle",
      junctionName = "Siddhartha Medical College & NH16 Service Rd",
      area = "Medical Zone, Vijayawada",
      latitude = 16.5020,
      longitude = 80.6650,
      officerName = "SI K. Lakshmi Narayana",
      officerPhone = "+91 94407 96214",
      officerBadge = "AP-VJA-1055",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 07:20 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    ),
    TrafficBoothEntity(
      id = 15,
      boothCode = "TB-15",
      name = "Enikepadu NH16 Ring",
      junctionName = "Outer Ring Road & Gannavaram Transit",
      area = "East Gateway, Vijayawada",
      latitude = 16.5260,
      longitude = 80.7020,
      officerName = "ASI N. Venkat Rao",
      officerPhone = "+91 94407 96215",
      officerBadge = "AP-VJA-1194",
      equipmentStatus = "NORMAL",
      lastInspection = "Today, 06:30 AM",
      cctvConnected = true,
      firstAidBoxModel = "AP-TRAUMA-KIT-V2 (Wall Mount + Rapid Deploy Belt)"
    )
  )

  fun getInitialItems(booths: List<TrafficBoothEntity>): List<FirstAidItemEntity> {
    val items = mutableListOf<FirstAidItemEntity>()
    val now = System.currentTimeMillis()
    val dayMillis = 24L * 60 * 60 * 1000

    booths.forEach { booth ->
      // Standard Trauma Kit Inventory items
      val isBooth4 = booth.id == 4
      val isBooth6 = booth.id == 6

      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "CAT Tourniquet Gen 7",
          category = "Hemorrhage Control",
          currentStock = if (isBooth4) 1 else 3,
          minThreshold = 2,
          unit = "units",
          expiryDate = "15 Nov 2028",
          expiryTimestamp = now + (365L * 3 * dayMillis),
          batchNumber = "CAT-AP-891",
          locationInKit = "Compartment A (Rapid Red Pull)",
          instructions = "Apply 2-3 inches above bleeding wound on limb. Turn windlass rod until arterial bleeding ceases. Write time on white band."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Sterile Trauma Bandage (6-inch)",
          category = "Hemorrhage Control",
          currentStock = if (isBooth4) 1 else 4,
          minThreshold = 3,
          unit = "packs",
          expiryDate = "20 Aug 2027",
          expiryTimestamp = now + (300L * dayMillis),
          batchNumber = "TB6-AP-440",
          locationInKit = "Compartment A (Hemorrhage)",
          instructions = "Place sterile pad directly over wound. Wrap elastic wrap through pressure bar and reverse direction to lock direct pressure."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "CPR Pocket Resuscitator Mask",
          category = "Airway & Resuscitation",
          currentStock = 2,
          minThreshold = 1,
          unit = "units",
          expiryDate = "10 Jan 2029",
          expiryTimestamp = now + (800L * dayMillis),
          batchNumber = "CPR-AP-102",
          locationInKit = "Compartment B (Airway)",
          instructions = "Position mask over victim nose and mouth. Perform head-tilt chin-lift. Deliver 1 breath every 5-6 seconds or alongside 30:2 CPR compressions."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Portable Emergency O2 Canister (6L)",
          category = "Airway & Resuscitation",
          currentStock = if (isBooth4) 1 else 2,
          minThreshold = 1,
          unit = "canister",
          expiryDate = "30 Dec 2027",
          expiryTimestamp = now + (450L * dayMillis),
          batchNumber = "O2-VJA-771",
          locationInKit = "Compartment B (Oxygen Bay)",
          instructions = "Attach inhaler mask. Press actuator trigger for continuous supplemental oxygen flow to conscious or breathing victim in shock."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Sterile Burn Hydrogel Dressing (10x10cm)",
          category = "Wound & Burn",
          currentStock = if (isBooth4) 2 else 5,
          minThreshold = 2,
          unit = "dressings",
          // For Booth 4: expiring in 14 days!
          expiryDate = if (isBooth4) "05 Oct 2026" else "24 Jun 2028",
          expiryTimestamp = if (isBooth4) now + (14L * dayMillis) else now + (600L * dayMillis),
          batchNumber = "BHD-AP-201",
          locationInKit = "Compartment C (Burns & Dressings)",
          instructions = "Cool burn with clear water first. Apply hydrogel sheet without rubbing. Do NOT pop blisters. Fasten loosely with sterile wrap."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Povidone Iodine 10% Solution (100ml)",
          category = "Antiseptics & Consumables",
          currentStock = 3,
          minThreshold = 2,
          unit = "bottles",
          // For Booth 6: Expired 5 days ago to demonstrate immediate expiry intimation!
          expiryDate = if (isBooth6) "16 Sep 2026" else "18 May 2028",
          expiryTimestamp = if (isBooth6) now - (5L * dayMillis) else now + (580L * dayMillis),
          batchNumber = if (isBooth6) "PVD-EXP-088" else "PVD-AP-930",
          locationInKit = "Compartment D (Antiseptics)",
          instructions = "Disinfect skin around abrasions and lacerations. Avoid pouring directly into deep puncture cavities."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "SAM Moldable Splint (36-inch)",
          category = "Fracture & Immobilization",
          currentStock = 2,
          minThreshold = 1,
          unit = "splints",
          expiryDate = "01 Jan 2030",
          expiryTimestamp = now + (1200L * dayMillis),
          batchNumber = "SAM-AP-312",
          locationInKit = "Back Pocket (Immobilization)",
          instructions = "Curve into C-curve or structural trough along injured limb or joint. Secure gently with roller gauze. Do not force bone back into place."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Cervical Rigid Collar (Adjustable)",
          category = "Fracture & Immobilization",
          currentStock = 2,
          minThreshold = 1,
          unit = "units",
          expiryDate = "15 Aug 2029",
          expiryTimestamp = now + (1000L * dayMillis),
          batchNumber = "CER-AP-114",
          locationInKit = "Back Pocket (Spine & Neck)",
          instructions = "Essential for two-wheeler accident victims. Stabilize cervical spine before moving patient. Adjust chin support notch."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Heavy-Duty Trauma Shears (7.5-inch)",
          category = "Antiseptics & Consumables",
          currentStock = 2,
          minThreshold = 1,
          unit = "units",
          expiryDate = "01 Jan 2032",
          expiryTimestamp = now + (2000L * dayMillis),
          batchNumber = "SHR-AP-007",
          locationInKit = "Front Sheath",
          instructions = "Cut through thick clothing, denim, leather jackets, seatbelts rapidly without injuring patient skin."
        )
      )
      items.add(
        FirstAidItemEntity(
          boothId = booth.id,
          name = "Instant Cold Ice Pack (Single-Use)",
          category = "Wound & Burn",
          currentStock = 4,
          minThreshold = 2,
          unit = "packs",
          expiryDate = "12 Nov 2027",
          expiryTimestamp = now + (400L * dayMillis),
          batchNumber = "ICE-AP-650",
          locationInKit = "Compartment C (Burns & Dressings)",
          instructions = "Squeeze firm to rupture inner water pouch and shake. Apply over sprains, blunt trauma, or swelling."
        )
      )
    }
    return items
  }

  fun getInitialIncidents(): List<EmergencyIncidentEntity> {
    val now = System.currentTimeMillis()
    val minuteMillis = 60 * 1000L

    return listOf(
      EmergencyIncidentEntity(
        id = 1,
        boothId = 1,
        boothName = "Benz Circle Traffic Booth",
        timestamp = now - (14 * minuteMillis), // Active incident within golden hour!
        severity = "CRITICAL_RED",
        victimCount = 1,
        injuryType = "Severe Arterial Bleed (Right Thigh Laceration)",
        vehicleTypes = "Motorcycle vs RTC Bus collision",
        firstAidAdministered = "CAT Tourniquet applied 2 inches above thigh wound at 07:31 AM. Sterile trauma bandage applied to stem secondary bleeding. Pressure maintained.",
        itemsUsedSummary = "1x CAT Tourniquet, 1x Trauma Bandage 6-inch",
        ambulanceStatus = "EN_ROUTE",
        ambulanceUnit = "108-AP-VJA-04 (GGH Trauma Ambulance)",
        ambulanceEtaMinutes = 3,
        hospitalDestination = "GGH Vijayawada (Trauma Emergency)",
        officerNotes = "Bleeding halted successfully with booth tourniquet within 3 mins of collision. Patient conscious and breathing. 108 ambulance approaching Benz Circle flyover."
      ),
      EmergencyIncidentEntity(
        id = 2,
        boothId = 4,
        boothName = "Ramavarappadu Ring",
        timestamp = now - (180 * minuteMillis),
        severity = "URGENT_YELLOW",
        victimCount = 2,
        injuryType = "Left Arm Fracture & Multiple Road Abrasions",
        vehicleTypes = "Auto-rickshaw tilt at roundabout",
        firstAidAdministered = "SAM Splint applied to immobilize left forearm. Betadine antiseptic cleaning and sterile gauze pads secured with adhesive tape.",
        itemsUsedSummary = "1x SAM Splint, 2x Gauze Pads, 1x Antiseptic wipe",
        ambulanceStatus = "TRANSFERRED_GGH",
        ambulanceUnit = "108-AP-VJA-12",
        ambulanceEtaMinutes = 0,
        hospitalDestination = "Pinnamaneni Siddhartha Hospital",
        officerNotes = "Both victims stabilized at booth within 5 minutes. Successfully transferred to trauma ward."
      ),
      EmergencyIncidentEntity(
        id = 3,
        boothId = 2,
        boothName = "Police Control Room Junction",
        timestamp = now - (24 * 60 * minuteMillis),
        severity = "MINOR_GREEN",
        victimCount = 1,
        injuryType = "Knee & Elbow Abrasions",
        vehicleTypes = "Bicycle skid on wet road",
        firstAidAdministered = "Cleaned with sterile eye wash saline, Betadine applied, sterile adhesive dressing applied.",
        itemsUsedSummary = "1x Adhesive Plasters, 1x Saline Pod",
        ambulanceStatus = "ARRIVED",
        ambulanceUnit = "Local Police First Responder",
        ambulanceEtaMinutes = 0,
        hospitalDestination = "Discharged on spot after first-aid",
        officerNotes = "Minor injury treated completely at traffic booth first aid station."
      )
    )
  }

  fun getInitialRequisitions(): List<SupplyRequisitionEntity> {
    val now = System.currentTimeMillis()
    val hourMillis = 60 * 60 * 1000L

    return listOf(
      SupplyRequisitionEntity(
        id = 1,
        boothId = 4,
        boothName = "Ramavarappadu Ring",
        itemName = "CAT Tourniquet Gen 7",
        quantityNeeded = 2,
        reason = "LOW_STOCK",
        requestedAt = now - (2 * hourMillis),
        status = "PENDING_DISPATCH",
        urgency = "URGENT"
      ),
      SupplyRequisitionEntity(
        id = 2,
        boothId = 6,
        boothName = "Bhavanipuram Junction",
        itemName = "Povidone Iodine 10% Solution",
        quantityNeeded = 3,
        reason = "EXPIRED",
        requestedAt = now - (4 * hourMillis),
        status = "PENDING_DISPATCH",
        urgency = "URGENT"
      ),
      SupplyRequisitionEntity(
        id = 3,
        boothId = 1,
        boothName = "Benz Circle Traffic Booth",
        itemName = "Sterile Trauma Bandage (6-inch)",
        quantityNeeded = 3,
        reason = "EMERGENCY_USAGE",
        requestedAt = now - (15 * 60 * 1000L),
        status = "PENDING_DISPATCH",
        urgency = "URGENT"
      )
    )
  }
}
