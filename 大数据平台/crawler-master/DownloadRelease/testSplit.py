srcname1 = "/home/fdse/Downloads/AndroidRelease/Computational Demonology//ComputationalDemonology-0.1/"
srcname2 = "/home/fdse/Downloads/AndroidRelease/Yellr//yellr-android-0.1.8/"
index1 = srcname1.find(srcname1.split('/')[-2])

index2 = srcname2.find(srcname2.split('/')[-2])
print srcname1[:index1-1] + srcname1[index1:]
print srcname2[:index2-1] + srcname2[index2:]
print srcname1.replace("//","/")

apilevel = "api apilevel-4"
print apilevel[apilevel.find('-')+1:]
