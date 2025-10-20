/*
 * Decompiled with CFR 0.152.
 */
package oracle.jdbc.driver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Map;
import oracle.jdbc.driver.Accessor;
import oracle.jdbc.driver.DatabaseError;
import oracle.jdbc.driver.OracleStatement;
import oracle.jdbc.driver.Representation;
import oracle.jdbc.logging.annotations.DefaultLogger;
import oracle.jdbc.logging.annotations.Feature;
import oracle.jdbc.logging.annotations.Supports;
import oracle.sql.Datum;
import oracle.sql.NUMBER;

@DefaultLogger(value="oracle.jdbc")
@Supports(value={Feature.COLUMN_GET, Feature.RESULT_FETCH})
abstract class NumberCommonAccessor
extends Accessor {
    protected final byte[] tmpBytes;
    static final boolean GET_XXX_ROUNDS = false;
    int[] digs = new int[27];
    static final int LNXSGNBT = 128;
    static final byte LNXDIGS = 20;
    static final byte LNXEXPBS = 64;
    static final int LNXEXPMX = 127;
    static final BigDecimal BIGDEC_ZERO = BigDecimal.valueOf(0L);
    static final byte MAX_LONG_EXPONENT = 9;
    static final byte MIN_LONG_EXPONENT = 9;
    static final byte MAX_INT_EXPONENT = 4;
    static final byte MIN_INT_EXPONENT = 4;
    static final byte MAX_SHORT_EXPONENT = 2;
    static final byte MIN_SHORT_EXPONENT = 2;
    static final byte MAX_BYTE_EXPONENT = 1;
    static final byte MIN_BYTE_EXPONENT = 1;
    static final int[] MAX_LONG = new int[]{202, 10, 23, 34, 73, 4, 69, 55, 78, 59, 8};
    static final int[] MIN_LONG = new int[]{53, 92, 79, 68, 29, 98, 33, 47, 24, 43, 93, 102};
    static final int MAX_LONG_length = 11;
    static final int MIN_LONG_length = 12;
    static final double[] factorTable = new double[]{1.0E254, 1.0E252, 1.0E250, 1.0E248, 1.0E246, 1.0E244, 1.0E242, 1.0E240, 1.0E238, 1.0E236, 1.0E234, 1.0E232, 1.0E230, 1.0E228, 1.0E226, 1.0E224, 1.0E222, 1.0E220, 1.0E218, 1.0E216, 1.0E214, 1.0E212, 1.0E210, 1.0E208, 1.0E206, 1.0E204, 1.0E202, 1.0E200, 1.0E198, 1.0E196, 1.0E194, 1.0E192, 1.0E190, 1.0E188, 1.0E186, 1.0E184, 1.0E182, 1.0E180, 1.0E178, 1.0E176, 1.0E174, 1.0E172, 1.0E170, 1.0E168, 1.0E166, 1.0E164, 1.0E162, 1.0E160, 1.0E158, 1.0E156, 1.0E154, 1.0E152, 1.0E150, 1.0E148, 1.0E146, 1.0E144, 1.0E142, 1.0E140, 1.0E138, 1.0E136, 1.0E134, 1.0E132, 1.0E130, 1.0E128, 1.0E126, 1.0E124, 1.0E122, 1.0E120, 1.0E118, 1.0E116, 1.0E114, 1.0E112, 1.0E110, 1.0E108, 1.0E106, 1.0E104, 1.0E102, 1.0E100, 1.0E98, 1.0E96, 1.0E94, 1.0E92, 1.0E90, 1.0E88, 1.0E86, 1.0E84, 1.0E82, 1.0E80, 1.0E78, 1.0E76, 1.0E74, 1.0E72, 1.0E70, 1.0E68, 1.0E66, 1.0E64, 1.0E62, 1.0E60, 1.0E58, 1.0E56, 1.0E54, 1.0E52, 1.0E50, 1.0E48, 1.0E46, 1.0E44, 1.0E42, 1.0E40, 1.0E38, 1.0E36, 1.0E34, 1.0E32, 1.0E30, 1.0E28, 1.0E26, 1.0E24, 1.0E22, 1.0E20, 1.0E18, 1.0E16, 1.0E14, 1.0E12, 1.0E10, 1.0E8, 1000000.0, 10000.0, 100.0, 1.0, 0.01, 1.0E-4, 1.0E-6, 1.0E-8, 1.0E-10, 1.0E-12, 1.0E-14, 1.0E-16, 1.0E-18, 1.0E-20, 1.0E-22, 1.0E-24, 1.0E-26, 1.0E-28, 1.0E-30, 1.0E-32, 1.0E-34, 1.0E-36, 1.0E-38, 1.0E-40, 1.0E-42, 1.0E-44, 1.0E-46, 1.0E-48, 1.0E-50, 1.0E-52, 1.0E-54, 1.0E-56, 1.0E-58, 1.0E-60, 1.0E-62, 1.0E-64, 1.0E-66, 1.0E-68, 1.0E-70, 1.0E-72, 1.0E-74, 1.0E-76, 1.0E-78, 1.0E-80, 1.0E-82, 1.0E-84, 1.0E-86, 1.0E-88, 1.0E-90, 1.0E-92, 1.0E-94, 1.0E-96, 1.0E-98, 1.0E-100, 1.0E-102, 1.0E-104, 1.0E-106, 1.0E-108, 1.0E-110, 1.0E-112, 1.0E-114, 1.0E-116, 1.0E-118, 1.0E-120, 1.0E-122, 1.0E-124, 1.0E-126, 1.0E-128, 1.0E-130, 1.0E-132, 1.0E-134, 1.0E-136, 1.0E-138, 1.0E-140, 1.0E-142, 1.0E-144, 1.0E-146, 1.0E-148, 1.0E-150, 1.0E-152, 1.0E-154, 1.0E-156, 1.0E-158, 1.0E-160, 1.0E-162, 1.0E-164, 1.0E-166, 1.0E-168, 1.0E-170, 1.0E-172, 1.0E-174, 1.0E-176, 1.0E-178, 1.0E-180, 1.0E-182, 1.0E-184, 1.0E-186, 1.0E-188, 1.0E-190, 1.0E-192, 1.0E-194, 1.0E-196, 1.0E-198, 1.0E-200, 1.0E-202, 1.0E-204, 1.0E-206, 1.0E-208, 1.0E-210, 1.0E-212, 1.0E-214, 1.0E-216, 1.0E-218, 1.0E-220, 1.0E-222, 1.0E-224, 1.0E-226, 1.0E-228, 1.0E-230, 1.0E-232, 1.0E-234, 1.0E-236, 1.0E-238, 1.0E-240, 1.0E-242, 1.0E-244, 1.0E-246, 1.0E-248, 1.0E-250, 1.0E-252, 1.0E-254};
    static final double[] small10pow = new double[]{1.0, 10.0, 100.0, 1000.0, 10000.0, 100000.0, 1000000.0, 1.0E7, 1.0E8, 1.0E9, 1.0E10, 1.0E11, 1.0E12, 1.0E13, 1.0E14, 1.0E15, 1.0E16, 1.0E17, 1.0E18, 1.0E19, 1.0E20, 1.0E21, 1.0E22};
    static final int tablemax = factorTable.length;
    static final double tablemaxexponent = 127.0;
    static final double tableminexponent = 127.0 - (double)(tablemax - 20);
    static final int MANTISSA_SIZE = 53;
    static final int[] expdigs0 = new int[]{25597, 55634, 18440, 18324, 42485, 50370, 56862, 11593, 45703, 57341, 10255, 12549, 59579, 5};
    static final int[] expdigs1 = new int[]{50890, 19916, 24149, 23777, 11324, 41057, 14921, 56274, 30917, 19462, 54968, 47943, 38791, 3872};
    static final int[] expdigs2 = new int[]{24101, 29690, 40218, 29073, 29604, 22037, 27674, 9082, 56670, 55244, 20865, 54874, 47573, 38};
    static final int[] expdigs3 = new int[]{22191, 40873, 1607, 45622, 23883, 24544, 32988, 43530, 61694, 55616, 43150, 32976, 27418, 25379};
    static final int[] expdigs4 = new int[]{55927, 44317, 6569, 54851, 238, 63160, 51447, 12231, 55667, 25459, 5674, 40962, 52047, 253};
    static final int[] expdigs5 = new int[]{56264, 8962, 51839, 64773, 39323, 49783, 15587, 30924, 36601, 56615, 27581, 36454, 35254, 2};
    static final int[] expdigs6 = new int[]{21545, 25466, 59727, 37873, 13099, 7602, 15571, 49963, 37664, 46896, 14328, 59258, 17403, 1663};
    static final int[] expdigs7 = new int[]{12011, 4842, 3874, 57395, 38141, 46606, 49307, 60792, 31833, 21440, 9318, 47123, 41461, 16};
    static final int[] expdigs8 = new int[]{52383, 25023, 56409, 43947, 51036, 17420, 62725, 5735, 53692, 44882, 64439, 36137, 24719, 10900};
    static final int[] expdigs9 = new int[]{65404, 27119, 57580, 26653, 42453, 19179, 26186, 42000, 1847, 62708, 14406, 12813, 247, 109};
    static final int[] expdigs10 = new int[]{36698, 50078, 40552, 35000, 49576, 56552, 261, 49572, 31475, 59609, 45363, 46658, 5900, 1};
    static final int[] expdigs11 = new int[]{33321, 54106, 42443, 60698, 47535, 24088, 45785, 18352, 47026, 40291, 5183, 35843, 24059, 714};
    static final int[] expdigs12 = new int[]{12129, 44450, 22706, 34030, 37175, 8760, 31915, 56544, 23407, 52176, 7260, 41646, 9415, 7};
    static final int[] expdigs13 = new int[]{43054, 17160, 43698, 6780, 36385, 52800, 62346, 52747, 33988, 2855, 31979, 38083, 44325, 4681};
    static final int[] expdigs14 = new int[]{60723, 40803, 16165, 19073, 2985, 9703, 41911, 37227, 41627, 1994, 38986, 27250, 53527, 46};
    static final int[] expdigs15 = new int[]{36481, 57623, 45627, 58488, 53274, 7238, 2063, 31221, 62631, 25319, 35409, 25293, 54667, 30681};
    static final int[] expdigs16 = new int[]{52138, 47106, 3077, 4517, 41165, 38738, 39997, 10142, 13078, 16637, 53438, 54647, 53630, 306};
    static final int[] expdigs17 = new int[]{25425, 24719, 55736, 8564, 12208, 3664, 51518, 17140, 61079, 30312, 2500, 30693, 4468, 3};
    static final int[] expdigs18 = new int[]{58368, 65134, 52675, 3178, 26300, 7986, 11833, 515, 23109, 63525, 29138, 19030, 50114, 2010};
    static final int[] expdigs19 = new int[]{41216, 15724, 12323, 26246, 59245, 58406, 46648, 13767, 11372, 15053, 61895, 48686, 7054, 20};
    static final int[] expdigs20 = new int[]{0, 29248, 62416, 1433, 14025, 43846, 39905, 44375, 137, 47955, 62409, 33386, 48983, 13177};
    static final int[] expdigs21 = new int[]{0, 21264, 53708, 60962, 25043, 64008, 31200, 50906, 9831, 56185, 43877, 36378, 50952, 131};
    static final int[] expdigs22 = new int[]{0, 50020, 25440, 60247, 44814, 39961, 6865, 26068, 34832, 9081, 17478, 44928, 20825, 1};
    static final int[] expdigs23 = new int[]{0, 0, 52929, 10084, 25506, 6346, 61348, 31525, 52689, 61296, 27615, 15903, 40426, 863};
    static final int[] expdigs24 = new int[]{0, 16384, 24122, 53840, 43508, 13170, 51076, 37670, 58198, 31414, 57292, 61762, 41691, 8};
    static final int[] expdigs25 = new int[]{0, 0, 4096, 29077, 42481, 30581, 10617, 59493, 46251, 1892, 5557, 4505, 52391, 5659};
    static final int[] expdigs26 = new int[]{0, 0, 58368, 11431, 1080, 29797, 47947, 36639, 42405, 50481, 29546, 9875, 39190, 56};
    static final int[] expdigs27 = new int[]{0, 0, 0, 57600, 63028, 53094, 12749, 18174, 21993, 48265, 14922, 59933, 4030, 37092};
    static final int[] expdigs28 = new int[]{0, 0, 0, 576, 1941, 35265, 9302, 42780, 50682, 28007, 29640, 28124, 60333, 370};
    static final int[] expdigs29 = new int[]{0, 0, 0, 5904, 8539, 12149, 36793, 43681, 12958, 60573, 21267, 35015, 46478, 3};
    static final int[] expdigs30 = new int[]{0, 0, 0, 0, 7268, 50548, 47962, 3644, 22719, 26999, 41893, 7421, 56711, 2430};
    static final int[] expdigs31 = new int[]{0, 0, 0, 0, 7937, 49002, 60772, 28216, 38893, 55975, 63988, 59711, 20227, 24};
    static final int[] expdigs32 = new int[]{0, 0, 0, 16384, 38090, 63404, 55657, 8801, 62648, 13666, 57656, 60234, 15930};
    static final int[] expdigs33 = new int[]{0, 0, 0, 4096, 37081, 37989, 16940, 55138, 17665, 39458, 9751, 20263, 159};
    static final int[] expdigs34 = new int[]{0, 0, 0, 58368, 35104, 16108, 61773, 14313, 30323, 54789, 57113, 38868, 1};
    static final int[] expdigs35 = new int[]{0, 0, 0, 8448, 18701, 29652, 51080, 65023, 27172, 37903, 3192, 1044};
    static final int[] expdigs36 = new int[]{0, 0, 0, 37440, 63101, 2917, 39177, 50457, 25830, 50186, 28867, 10};
    static final int[] expdigs37 = new int[]{0, 0, 0, 56080, 45850, 37384, 3668, 12301, 38269, 18196, 6842};
    static final int[] expdigs38 = new int[]{0, 0, 0, 46436, 13565, 50181, 34770, 37478, 5625, 27707, 68};
    static final int[] expdigs39 = new int[]{0, 0, 0, 32577, 45355, 38512, 38358, 3651, 36101, 44841};
    static final int[] expdigs40 = new int[]{0, 0, 16384, 28506, 5696, 56746, 15456, 50499, 27230, 448};
    static final int[] expdigs41 = new int[]{0, 0, 4096, 285, 9232, 58239, 57170, 38515, 31729, 4};
    static final int[] expdigs42 = new int[]{0, 0, 58368, 41945, 57108, 12378, 28752, 48226, 2938};
    static final int[] expdigs43 = new int[]{0, 0, 24832, 47605, 49067, 23716, 61891, 25385, 29};
    static final int[] expdigs44 = new int[]{0, 0, 8768, 2442, 50298, 23174, 19624, 19259};
    static final int[] expdigs45 = new int[]{0, 0, 40720, 45899, 1813, 31689, 38862, 192};
    static final int[] expdigs46 = new int[]{0, 0, 36452, 14221, 34752, 48813, 60681, 1};
    static final int[] expdigs47 = new int[]{0, 0, 61313, 34220, 16731, 11629, 1262};
    static final int[] expdigs48 = new int[]{0, 16384, 60906, 18036, 40144, 40748, 12};
    static final int[] expdigs49 = new int[]{0, 4096, 609, 15909, 52830, 8271};
    static final int[] expdigs50 = new int[]{0, 58368, 3282, 56520, 47058, 82};
    static final int[] expdigs51 = new int[]{0, 41216, 52461, 7118, 54210};
    static final int[] expdigs52 = new int[]{0, 45632, 51642, 6624, 542};
    static final int[] expdigs53 = new int[]{0, 25360, 24109, 27591, 5};
    static final int[] expdigs54 = new int[]{0, 42852, 46771, 3552};
    static final int[] expdigs55 = new int[]{0, 28609, 34546, 35};
    static final int[] expdigs56 = new int[]{16384, 4218, 23283};
    static final int[] expdigs57 = new int[]{4096, 54437, 232};
    static final int[] expdigs58 = new int[]{58368, 21515, 2};
    static final int[] expdigs59 = new int[]{57600, 1525};
    static final int[] expdigs60 = new int[]{16960, 15};
    static final int[] expdigs61 = new int[]{10000};
    static final int[] expdigs62 = new int[]{100};
    static final int[] expdigs63 = new int[]{1};
    static final int[] expdigs64 = new int[]{36700, 62914, 23592, 49807, 10485, 36700, 62914, 23592, 49807, 10485, 36700, 62914, 23592, 655};
    static final int[] expdigs65 = new int[]{14784, 18979, 33659, 19503, 2726, 9542, 629, 2202, 40475, 10590, 4299, 47815, 36280, 6};
    static final int[] expdigs66 = new int[]{16332, 9978, 33613, 31138, 35584, 64252, 13857, 14424, 62281, 46279, 36150, 46573, 63392, 4294};
    static final int[] expdigs67 = new int[]{6716, 24348, 22618, 23904, 21327, 3919, 44703, 19149, 28803, 48959, 6259, 50273, 62237, 42};
    static final int[] expdigs68 = new int[]{8471, 23660, 38254, 26440, 33662, 38879, 9869, 11588, 41479, 23225, 60127, 24310, 32615, 28147};
    static final int[] expdigs69 = new int[]{13191, 6790, 63297, 30410, 12788, 42987, 23691, 28296, 32527, 38898, 41233, 4830, 31128, 281};
    static final int[] expdigs70 = new int[]{4064, 53152, 62236, 29139, 46658, 12881, 31694, 4870, 19986, 24637, 9587, 28884, 53395, 2};
    static final int[] expdigs71 = new int[]{26266, 10526, 16260, 55017, 35680, 40443, 19789, 17356, 30195, 55905, 28426, 63010, 44197, 1844};
    static final int[] expdigs72 = new int[]{38273, 7969, 37518, 26764, 23294, 63974, 18547, 17868, 24550, 41191, 17323, 53714, 29277, 18};
    static final int[] expdigs73 = new int[]{16739, 37738, 38090, 26589, 43521, 1543, 15713, 10671, 11975, 41533, 18106, 9348, 16921, 12089};
    static final int[] expdigs74 = new int[]{14585, 61981, 58707, 16649, 25994, 39992, 28337, 17801, 37475, 22697, 31638, 16477, 58496, 120};
    static final int[] expdigs75 = new int[]{58472, 2585, 40564, 27691, 44824, 27269, 58610, 54572, 35108, 30373, 35050, 10650, 13692, 1};
    static final int[] expdigs76 = new int[]{50392, 58911, 41968, 49557, 29112, 29939, 43526, 63500, 55595, 27220, 25207, 38361, 18456, 792};
    static final int[] expdigs77 = new int[]{26062, 32046, 3696, 45060, 46821, 40931, 50242, 60272, 24148, 20588, 6150, 44948, 60477, 7};
    static final int[] expdigs78 = new int[]{12430, 30407, 320, 41980, 58777, 41755, 41041, 13609, 45167, 13348, 40838, 60354, 19454, 5192};
    static final int[] expdigs79 = new int[]{30926, 26518, 13110, 43018, 54982, 48258, 24658, 15209, 63366, 11929, 20069, 43857, 60487, 51};
    static final int[] expdigs80 = new int[]{51263, 54048, 48761, 48627, 30576, 49046, 4414, 61195, 61755, 48474, 19124, 55906, 15511, 34028};
    static final int[] expdigs81 = new int[]{39834, 11681, 47018, 3107, 64531, 54229, 41331, 41899, 51735, 42427, 59173, 13010, 18505, 340};
    static final int[] expdigs82 = new int[]{27268, 6670, 31272, 9861, 45865, 10372, 12865, 62678, 23454, 35158, 20252, 29621, 26399, 3};
    static final int[] expdigs83 = new int[]{57738, 46147, 66, 48154, 11239, 21430, 55809, 46003, 15044, 25138, 52780, 48043, 4883, 2230};
    static final int[] expdigs84 = new int[]{20893, 62065, 64225, 52254, 59094, 55919, 60195, 5702, 48647, 50058, 7736, 41768, 19709, 22};
    static final int[] expdigs85 = new int[]{37714, 32321, 45840, 36031, 33290, 47121, 5146, 28127, 9887, 25390, 52929, 2698, 1073, 14615};
    static final int[] expdigs86 = new int[]{35111, 8187, 18153, 56721, 40309, 59453, 51824, 4868, 45974, 3530, 43783, 8546, 9841, 146};
    static final int[] expdigs87 = new int[]{23288, 61030, 42779, 19572, 29894, 47780, 45082, 32816, 43713, 33458, 25341, 63655, 30244, 1};
    static final int[] expdigs88 = new int[]{58138, 33000, 62869, 37127, 61799, 298, 46353, 5693, 63898, 62040, 989, 23191, 53065, 957};
    static final int[] expdigs89 = new int[]{42524, 32442, 36673, 15444, 22900, 658, 61412, 32824, 21610, 64190, 1975, 11373, 37886, 9};
    static final int[] expdigs90 = new int[]{26492, 4357, 32437, 10852, 34233, 53968, 55056, 34692, 64553, 38226, 41929, 21646, 6667, 6277};
    static final int[] expdigs91 = new int[]{61213, 698, 16053, 50571, 2963, 50347, 13657, 48188, 46520, 19387, 33187, 25775, 50529, 62};
    static final int[] expdigs92 = new int[]{42864, 54351, 45226, 20476, 23443, 17724, 3780, 44701, 52910, 23402, 28374, 46862, 40234, 41137};
    static final int[] expdigs93 = new int[]{23366, 62147, 58123, 44113, 55284, 39498, 3314, 9622, 9704, 27759, 25187, 43722, 24650, 411};
    static final int[] expdigs94 = new int[]{38899, 44530, 19586, 37141, 1863, 9570, 32801, 31553, 51870, 62536, 51369, 30583, 7455, 4};
    static final int[] expdigs95 = new int[]{10421, 4321, 43699, 3472, 65252, 17057, 13858, 29819, 14733, 21490, 40602, 31315, 65186, 2695};
    static final int[] expdigs96 = new int[]{6002, 54438, 29272, 34113, 17036, 25074, 36183, 953, 25051, 12011, 20722, 4245, 62911, 26};
    static final int[] expdigs97 = new int[]{14718, 45935, 8408, 42891, 21312, 56531, 44159, 45581, 20325, 36295, 35509, 24455, 30844, 17668};
    static final int[] expdigs98 = new int[]{54542, 45023, 23021, 3050, 31015, 20881, 50904, 40432, 33626, 14125, 44264, 60537, 44872, 176};
    static final int[] expdigs99 = new int[]{60183, 8969, 14648, 17725, 11451, 50016, 34587, 46279, 19341, 42084, 16826, 5848, 50256, 1};
    static final int[] expdigs100 = new int[]{64999, 53685, 60382, 19151, 25736, 5357, 31302, 23283, 14225, 52622, 56781, 39489, 60351, 1157};
    static final int[] expdigs101 = new int[]{1305, 4469, 39270, 18541, 63827, 59035, 54707, 16616, 32910, 48367, 64137, 2360, 37959, 11};
    static final int[] expdigs102 = new int[]{45449, 32125, 19705, 56098, 51958, 5225, 18285, 13654, 9341, 25888, 50946, 26855, 36068, 7588};
    static final int[] expdigs103 = new int[]{27324, 53405, 43450, 25464, 3796, 3329, 46058, 53220, 26307, 53998, 33932, 23861, 58032, 75};
    static final int[] expdigs104 = new int[]{63080, 50735, 1844, 21406, 57926, 63607, 24936, 52889, 23469, 64488, 539, 8859, 21210, 49732};
    static final int[] expdigs105 = new int[]{62890, 39828, 3950, 32982, 39245, 21607, 40226, 50991, 18584, 10475, 59643, 40720, 21183, 497};
    static final int[] expdigs106 = new int[]{37329, 64623, 11835, 985, 46923, 48712, 28582, 21481, 28366, 41392, 13703, 49559, 63781, 4};
    static final int[] expdigs107 = new int[]{3316, 60011, 41933, 47959, 54404, 39790, 12283, 941, 46090, 42226, 18108, 38803, 16879, 3259};
    static final int[] expdigs108 = new int[]{46563, 56305, 5006, 45044, 49040, 12849, 778, 6563, 46336, 3043, 7390, 2354, 38835, 32};
    static final int[] expdigs109 = new int[]{28653, 3742, 33331, 2671, 39772, 29981, 56489, 1973, 26280, 26022, 56391, 56434, 57039, 21359};
    static final int[] expdigs110 = new int[]{9461, 17732, 7542, 26241, 8917, 24548, 61513, 13126, 59245, 41547, 1874, 41852, 39236, 213};
    static final int[] expdigs111 = new int[]{36794, 22459, 63645, 14024, 42032, 53329, 25518, 11272, 18287, 20076, 62933, 3039, 8912, 2};
    static final int[] expdigs112 = new int[]{14926, 15441, 32337, 42579, 26354, 35154, 22815, 36955, 12564, 8047, 856, 41917, 55080, 1399};
    static final int[] expdigs113 = new int[]{8668, 50617, 10153, 17465, 1574, 28532, 15301, 58041, 38791, 60373, 663, 29255, 65431, 13};
    static final int[] expdigs114 = new int[]{21589, 32199, 24754, 45321, 9349, 26230, 35019, 37508, 20896, 42986, 31405, 12458, 65173, 9173};
    static final int[] expdigs115 = new int[]{46746, 1632, 61196, 50915, 64318, 41549, 2971, 23968, 59191, 58756, 61917, 779, 48493, 91};
    static final int[] expdigs116 = new int[]{1609, 63382, 15744, 15685, 51627, 56348, 33838, 52458, 44148, 11077, 56293, 41906, 45227, 60122};
    static final int[] expdigs117 = new int[]{19676, 45198, 6055, 38823, 8380, 49060, 17377, 58196, 43039, 21737, 59545, 12870, 14870, 601};
    static final int[] expdigs118 = new int[]{4128, 2418, 28241, 13495, 26298, 3767, 31631, 5169, 8950, 27087, 56956, 4060, 804, 6};
    static final int[] expdigs119 = new int[]{39930, 40673, 19029, 54677, 38145, 23200, 41325, 24564, 24955, 54484, 23863, 52998, 13147, 3940};
    static final int[] expdigs120 = new int[]{3676, 24655, 34924, 27416, 23974, 887, 10899, 4833, 21221, 28725, 19899, 57546, 26345, 39};
    static final int[] expdigs121 = new int[]{28904, 41324, 18596, 42292, 12070, 52013, 30810, 61057, 55753, 32324, 38953, 6752, 32688, 25822};
    static final int[] expdigs122 = new int[]{42232, 26627, 2807, 27948, 50583, 49016, 32420, 64180, 3178, 3600, 21361, 52496, 14744, 258};
    static final int[] expdigs123 = new int[]{2388, 59904, 28863, 7488, 31963, 8354, 47510, 15059, 2653, 58363, 31670, 21496, 38158, 2};
    static final int[] expdigs124 = new int[]{50070, 5266, 26158, 10774, 15148, 6873, 30230, 33898, 63720, 51799, 4515, 50124, 19875, 1692};
    static final int[] expdigs125 = new int[]{54240, 3984, 12058, 2729, 13914, 11865, 38313, 39660, 10467, 20834, 36745, 57517, 60491, 16};
    static final int[] expdigs126 = new int[]{5387, 58214, 9214, 13883, 14445, 34873, 21745, 13490, 23334, 25008, 58535, 19372, 44484, 11090};
    static final int[] expdigs127 = new int[]{27578, 64807, 12543, 794, 13907, 61297, 12013, 64360, 15961, 20566, 24178, 15922, 59427, 110};
    static final int[] expdigs128 = new int[]{49427, 41935, 46000, 59645, 45358, 51075, 15848, 32756, 38170, 14623, 35631, 57175, 7147, 1};
    static final int[] expdigs129 = new int[]{33941, 39160, 55469, 45679, 22878, 60091, 37210, 18508, 1638, 57398, 65026, 41643, 54966, 726};
    static final int[] expdigs130 = new int[]{60632, 24639, 41842, 62060, 20544, 59583, 52800, 1495, 48513, 43827, 10480, 1727, 17589, 7};
    static final int[] expdigs131 = new int[]{5590, 60244, 53985, 26632, 53049, 33628, 58267, 54922, 21641, 62744, 58109, 2070, 26887, 4763};
    static final int[] expdigs132 = new int[]{62970, 37957, 34618, 29757, 24123, 2302, 17622, 58876, 44780, 6525, 33349, 36065, 41556, 47};
    static final int[] expdigs133 = new int[]{1615, 24878, 20040, 11487, 23235, 27766, 59005, 57847, 60881, 11588, 63635, 61281, 31817, 31217};
    static final int[] expdigs134 = new int[]{14434, 2870, 65081, 44023, 40864, 40254, 47120, 6476, 32066, 23053, 17020, 19618, 11459, 312};
    static final int[] expdigs135 = new int[]{43398, 40005, 36695, 8304, 12205, 16131, 42414, 38075, 63890, 2851, 61774, 59833, 7978, 3};
    static final int[] expdigs136 = new int[]{56426, 22060, 15473, 31824, 19088, 38788, 64386, 12875, 35770, 65519, 11824, 19623, 56959, 2045};
    static final int[] expdigs137 = new int[]{16292, 32333, 10640, 47504, 29026, 30534, 23581, 6682, 10188, 24248, 44027, 51969, 30060, 20};
    static final int[] expdigs138 = new int[]{29432, 37518, 55373, 2727, 33243, 22572, 16689, 35625, 34145, 15830, 59880, 32552, 52948, 13407};
    static final int[] expdigs139 = new int[]{61898, 27244, 41841, 33450, 18682, 13988, 24415, 11497, 1652, 34237, 34677, 325, 5117, 134};
    static final int[] expdigs140 = new int[]{16347, 3549, 48915, 22616, 21158, 51913, 32356, 21086, 3293, 8862, 1002, 26873, 22333, 1};
    static final int[] expdigs141 = new int[]{25966, 63733, 28215, 31946, 40858, 58538, 11004, 6877, 6109, 3965, 35478, 37365, 45488, 878};
    static final int[] expdigs142 = new int[]{45479, 34060, 17321, 19980, 1719, 16314, 29601, 8588, 58388, 22321, 14117, 63288, 51572, 8};
    static final int[] expdigs143 = new int[]{46861, 47640, 11481, 23766, 46730, 53756, 8682, 60589, 42028, 27453, 29714, 31598, 39954, 5758};
    static final int[] expdigs144 = new int[]{29304, 58803, 51232, 27762, 60760, 17576, 19092, 26820, 11561, 48771, 6850, 27841, 38410, 57};
    static final int[] expdigs145 = new int[]{2916, 49445, 34666, 46387, 18627, 58279, 60468, 190, 3545, 51889, 51605, 47909, 40910, 37739};
    static final int[] expdigs146 = new int[]{19034, 62098, 15419, 33887, 38852, 53011, 28129, 37357, 11176, 48360, 9035, 9654, 25968, 377};
    static final int[] expdigs147 = new int[]{25094, 10451, 7363, 55389, 57404, 27399, 11422, 39695, 28947, 12935, 61694, 26310, 50722, 3};
    static final int[][] expdigstable = new int[][]{expdigs0, expdigs1, expdigs2, expdigs3, expdigs4, expdigs5, expdigs6, expdigs7, expdigs8, expdigs9, expdigs10, expdigs11, expdigs12, expdigs13, expdigs14, expdigs15, expdigs16, expdigs17, expdigs18, expdigs19, expdigs20, expdigs21, expdigs22, expdigs23, expdigs24, expdigs25, expdigs26, expdigs27, expdigs28, expdigs29, expdigs30, expdigs31, expdigs32, expdigs33, expdigs34, expdigs35, expdigs36, expdigs37, expdigs38, expdigs39, expdigs40, expdigs41, expdigs42, expdigs43, expdigs44, expdigs45, expdigs46, expdigs47, expdigs48, expdigs49, expdigs50, expdigs51, expdigs52, expdigs53, expdigs54, expdigs55, expdigs56, expdigs57, expdigs58, expdigs59, expdigs60, expdigs61, expdigs62, expdigs63, expdigs64, expdigs65, expdigs66, expdigs67, expdigs68, expdigs69, expdigs70, expdigs71, expdigs72, expdigs73, expdigs74, expdigs75, expdigs76, expdigs77, expdigs78, expdigs79, expdigs80, expdigs81, expdigs82, expdigs83, expdigs84, expdigs85, expdigs86, expdigs87, expdigs88, expdigs89, expdigs90, expdigs91, expdigs92, expdigs93, expdigs94, expdigs95, expdigs96, expdigs97, expdigs98, expdigs99, expdigs100, expdigs101, expdigs102, expdigs103, expdigs104, expdigs105, expdigs106, expdigs107, expdigs108, expdigs109, expdigs110, expdigs111, expdigs112, expdigs113, expdigs114, expdigs115, expdigs116, expdigs117, expdigs118, expdigs119, expdigs120, expdigs121, expdigs122, expdigs123, expdigs124, expdigs125, expdigs126, expdigs127, expdigs128, expdigs129, expdigs130, expdigs131, expdigs132, expdigs133, expdigs134, expdigs135, expdigs136, expdigs137, expdigs138, expdigs139, expdigs140, expdigs141, expdigs142, expdigs143, expdigs144, expdigs145, expdigs146, expdigs147};
    static final int[] nexpdigstable = new int[]{14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 13, 13, 13, 12, 12, 11, 11, 10, 10, 10, 9, 9, 8, 8, 8, 7, 7, 6, 6, 5, 5, 5, 4, 4, 3, 3, 3, 2, 2, 1, 1, 1, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14};
    static final int[] binexpstable = new int[]{90, 89, 89, 88, 88, 88, 87, 87, 86, 86, 86, 85, 85, 84, 84, 83, 83, 83, 82, 82, 81, 81, 81, 80, 80, 79, 79, 78, 78, 78, 77, 77, 76, 76, 76, 75, 75, 74, 74, 73, 73, 73, 72, 72, 71, 71, 71, 70, 70, 69, 69, 68, 68, 68, 67, 67, 66, 66, 66, 65, 65, 64, 64, 64, 63, 63, 62, 62, 61, 61, 61, 60, 60, 59, 59, 59, 58, 58, 57, 57, 56, 56, 56, 55, 55, 54, 54, 54, 53, 53, 52, 52, 51, 51, 51, 50, 50, 49, 49, 49, 48, 48, 47, 47, 46, 46, 46, 45, 45, 44, 44, 44, 43, 43, 42, 42, 41, 41, 41, 40, 40, 39, 39, 39, 38, 38, 37, 37, 37, 36, 36, 35, 35, 34, 34, 34, 33, 33, 32, 32, 32, 31, 31, 30, 30, 29, 29, 29};

    NumberCommonAccessor(OracleStatement oracleStatement, int n2, boolean bl) {
        super(Representation.NUMBER, oracleStatement, n2, bl);
        this.tmpBytes = new byte[this.representationMaxLength];
    }

    void init(OracleStatement oracleStatement, int n2, short s2, int n3, boolean bl) throws SQLException {
        this.init(oracleStatement, 6, 6, s2, bl);
        this.initForDataAccess(n3, n2, null);
    }

    void init(OracleStatement oracleStatement, int n2, int n3, boolean bl, int n4, int n5, int n6, long l2, int n7, short s2) throws SQLException {
        this.init(oracleStatement, 6, 6, s2, false);
        this.initForDescribe(n2, n3, bl, n4, n5, n6, l2, n7, s2, null);
        this.initForDataAccess(0, n3, null);
    }

    @Override
    void initForDataAccess(int n2, int n3, String string) throws SQLException {
        super.initForDataAccess(n2, n3, string);
        ++this.byteLength;
    }

    @Override
    int getInt(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        byte by = this.tmpBytes[0];
        int n4 = 0;
        if ((by & 0xFFFFFF80) != 0) {
            int n5;
            byte by2 = (byte)(n3 - 1);
            byte by3 = (byte)((by & 0xFFFFFF7F) - 65);
            int n6 = n5 = by2 > by3 + 1 ? by3 + 2 : by2 + 1;
            if (by3 >= 4) {
                int n7;
                if (by3 > 4) {
                    this.throwOverflow();
                }
                long l2 = 0L;
                if (n5 > 1) {
                    l2 = this.tmpBytes[1] - 1;
                    for (n7 = 2; n7 < n6; ++n7) {
                        l2 = l2 * 100L + (long)(this.tmpBytes[n7] - 1);
                    }
                }
                for (n7 = by3 - by2; n7 >= 0; --n7) {
                    l2 *= 100L;
                }
                if (l2 > Integer.MAX_VALUE) {
                    this.throwOverflow();
                }
                n4 = (int)l2;
            } else {
                int n8;
                if (n5 > 1) {
                    n4 = this.tmpBytes[1] - 1;
                    for (n8 = 2; n8 < n6; ++n8) {
                        n4 = n4 * 100 + (this.tmpBytes[n8] - 1);
                    }
                }
                for (n8 = by3 - by2; n8 >= 0; --n8) {
                    n4 *= 100;
                }
            }
        } else {
            int n9;
            byte by4 = (byte)((~by & 0xFFFFFF7F) - 65);
            byte by5 = (byte)(n3 - 1);
            if (by5 != 20 || this.tmpBytes[by5] == 102) {
                by5 = (byte)(by5 - 1);
            }
            int n10 = n9 = by5 > by4 + 1 ? by4 + 2 : by5 + 1;
            if (by4 >= 4) {
                int n11;
                if (by4 > 4) {
                    this.throwOverflow();
                }
                long l3 = 0L;
                if (n9 > 1) {
                    l3 = 101 - this.tmpBytes[1];
                    for (n11 = 2; n11 < n10; ++n11) {
                        l3 = l3 * 100L + (long)(101 - this.tmpBytes[n11]);
                    }
                }
                for (n11 = by4 - by5; n11 >= 0; --n11) {
                    l3 *= 100L;
                }
                if ((l3 = -l3) < Integer.MIN_VALUE) {
                    this.throwOverflow();
                }
                n4 = (int)l3;
            } else {
                int n12;
                if (n9 > 1) {
                    n4 = 101 - this.tmpBytes[1];
                    for (n12 = 2; n12 < n10; ++n12) {
                        n4 = n4 * 100 + (101 - this.tmpBytes[n12]);
                    }
                }
                for (n12 = by4 - by5; n12 >= 0; --n12) {
                    n4 *= 100;
                }
                n4 = -n4;
            }
        }
        return n4;
    }

    @Override
    boolean getBoolean(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return false;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        return n3 != 1 || this.tmpBytes[0] != -128;
    }

    @Override
    short getShort(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        byte by = this.tmpBytes[0];
        int n4 = 0;
        if ((by & 0xFFFFFF80) != 0) {
            int n5;
            byte by2;
            int n6;
            byte by3 = (byte)((by & 0xFFFFFF7F) - 65);
            if (by3 > 2) {
                this.throwOverflow();
            }
            int n7 = n6 = (by2 = (byte)(n3 - 1)) > by3 + 1 ? by3 + 2 : by2 + 1;
            if (n6 > 1) {
                n4 = this.tmpBytes[1] - 1;
                for (n5 = 2; n5 < n7; ++n5) {
                    n4 = n4 * 100 + (this.tmpBytes[n5] - 1);
                }
            }
            for (n5 = by3 - by2; n5 >= 0; --n5) {
                n4 *= 100;
            }
            if (by3 == 2 && n4 > Short.MAX_VALUE) {
                this.throwOverflow();
            }
        } else {
            int n8;
            int n9;
            byte by4;
            byte by5 = (byte)((~by & 0xFFFFFF7F) - 65);
            if (by5 > 2) {
                this.throwOverflow();
            }
            if ((by4 = (byte)(n3 - 1)) != 20 || this.tmpBytes[by4] == 102) {
                by4 = (byte)(by4 - 1);
            }
            int n10 = n9 = by4 > by5 + 1 ? by5 + 2 : by4 + 1;
            if (n9 > 1) {
                n4 = 101 - this.tmpBytes[1];
                for (n8 = 2; n8 < n10; ++n8) {
                    n4 = n4 * 100 + (101 - this.tmpBytes[n8]);
                }
            }
            for (n8 = by5 - by4; n8 >= 0; --n8) {
                n4 *= 100;
            }
            n4 = -n4;
            if (by5 == 2 && n4 < Short.MIN_VALUE) {
                this.throwOverflow();
            }
        }
        return (short)n4;
    }

    @Override
    byte getByte(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        byte by = this.tmpBytes[0];
        int n4 = 0;
        if ((by & 0xFFFFFF80) != 0) {
            byte by2;
            byte by3 = (byte)((by & 0xFFFFFF7F) - 65);
            if (by3 > 1) {
                this.throwOverflow();
            }
            if ((by2 = (byte)(n3 - 1)) > by3 + 1) {
                switch (by3) {
                    default: {
                        break;
                    }
                    case -1: {
                        break;
                    }
                    case 0: {
                        n4 = this.tmpBytes[1] - 1;
                        break;
                    }
                    case 1: {
                        n4 = (this.tmpBytes[1] - 1) * 100 + (this.tmpBytes[2] - 1);
                        if (n4 > 127) {
                            this.throwOverflow();
                            break;
                        } else {
                            break;
                        }
                    }
                }
            } else if (by2 == 1) {
                if (by3 == 1) {
                    n4 = (this.tmpBytes[1] - 1) * 100;
                    if (n4 > 127) {
                        this.throwOverflow();
                    }
                } else {
                    n4 = this.tmpBytes[1] - 1;
                }
            } else if (by2 == 2 && (n4 = (this.tmpBytes[1] - 1) * 100 + (this.tmpBytes[2] - 1)) > 127) {
                this.throwOverflow();
            }
        } else {
            byte by4;
            byte by5 = (byte)((~by & 0xFFFFFF7F) - 65);
            if (by5 > 1) {
                this.throwOverflow();
            }
            if ((by4 = (byte)(n3 - 1)) != 20 || this.tmpBytes[by4] == 102) {
                by4 = (byte)(by4 - 1);
            }
            if (by4 > by5 + 1) {
                switch (by5) {
                    default: {
                        break;
                    }
                    case -1: {
                        break;
                    }
                    case 0: {
                        n4 = -(101 - this.tmpBytes[1]);
                        break;
                    }
                    case 1: {
                        n4 = -((101 - this.tmpBytes[1]) * 100 + (101 - this.tmpBytes[2]));
                        if (n4 < -128) {
                            this.throwOverflow();
                            break;
                        } else {
                            break;
                        }
                    }
                }
            } else if (by4 == 1) {
                if (by5 == 1) {
                    n4 = -(101 - this.tmpBytes[1]) * 100;
                    if (n4 < -128) {
                        this.throwOverflow();
                    }
                } else {
                    n4 = -(101 - this.tmpBytes[1]);
                }
            } else if (by4 == 2 && (n4 = -((101 - this.tmpBytes[1]) * 100 + (101 - this.tmpBytes[2]))) < -128) {
                this.throwOverflow();
            }
        }
        return (byte)n4;
    }

    @Override
    long getLong(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0L;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        byte by = this.tmpBytes[0];
        long l2 = 0L;
        if ((by & 0xFFFFFF80) != 0) {
            byte by2;
            int n4;
            int n5;
            int n6;
            if (by == -128 && n3 == 1) {
                return 0L;
            }
            byte by3 = (byte)((by & 0xFFFFFF7F) - 65);
            if (by3 > 9) {
                this.throwOverflow();
            }
            if (by3 == 9) {
                n6 = 1;
                n5 = n3;
                if (n3 > 11) {
                    n5 = 11;
                }
                while (n6 < n5) {
                    n4 = this.tmpBytes[n6] & 0xFF;
                    int n7 = MAX_LONG[n6];
                    if (n4 != n7) {
                        if (n4 < n7) break;
                        this.throwOverflow();
                    }
                    ++n6;
                }
                if (n6 == n5 && n3 > 11) {
                    this.throwOverflow();
                }
            }
            n5 = n6 = (by2 = (byte)(n3 - 1)) > by3 + 1 ? by3 + 2 : by2 + 1;
            if (n6 > 1) {
                l2 = this.tmpBytes[1] - 1;
                for (n4 = 2; n4 < n5; ++n4) {
                    l2 = l2 * 100L + (long)(this.tmpBytes[n4] - 1);
                }
            }
            for (n4 = by3 - by2; n4 >= 0; --n4) {
                l2 *= 100L;
            }
        } else {
            byte by4;
            int n8;
            int n9;
            int n10;
            byte by5 = (byte)((~by & 0xFFFFFF7F) - 65);
            if (by5 > 9) {
                this.throwOverflow();
            }
            if (by5 == 9) {
                n10 = 1;
                n9 = n3;
                if (n3 > 12) {
                    n9 = 12;
                }
                while (n10 < n9) {
                    n8 = this.tmpBytes[n10] & 0xFF;
                    int n11 = MIN_LONG[n10];
                    if (n8 != n11) {
                        if (n8 > n11) break;
                        this.throwOverflow();
                    }
                    ++n10;
                }
                if (n10 == n9 && n3 < 12) {
                    this.throwOverflow();
                }
            }
            if ((by4 = (byte)(n3 - 1)) != 20 || this.tmpBytes[by4] == 102) {
                by4 = (byte)(by4 - 1);
            }
            n9 = n10 = by4 > by5 + 1 ? by5 + 2 : by4 + 1;
            if (n10 > 1) {
                l2 = 101 - this.tmpBytes[1];
                for (n8 = 2; n8 < n9; ++n8) {
                    l2 = l2 * 100L + (long)(101 - this.tmpBytes[n8]);
                }
            }
            for (n8 = by5 - by4; n8 >= 0; --n8) {
                l2 *= 100L;
            }
            l2 = -l2;
        }
        return l2;
    }

    @Override
    float getFloat(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0.0f;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        byte by = this.tmpBytes[0];
        double d2 = 0.0;
        int n4 = 1;
        if ((by & 0xFFFFFF80) != 0) {
            if (by == -128 && n3 == 1) {
                return 0.0f;
            }
            if (n3 == 2 && by == -1 && this.tmpBytes[1] == 101) {
                return Float.POSITIVE_INFINITY;
            }
            byte by2 = (byte)((by & 0xFFFFFF7F) - 65);
            int n5 = n3 - 1;
            while (this.tmpBytes[n4] == 1 && n5 > 0) {
                ++n4;
                --n5;
                by2 = (byte)(by2 - 1);
            }
            int n6 = (int)(127.0 - (double)by2);
            switch (n5) {
                case 1: {
                    d2 = (double)(this.tmpBytes[n4] - 1) * factorTable[n6];
                    break;
                }
                case 2: {
                    d2 = (double)((this.tmpBytes[n4] - 1) * 100 + (this.tmpBytes[n4 + 1] - 1)) * factorTable[n6 + 1];
                    break;
                }
                case 3: {
                    d2 = (double)((this.tmpBytes[n4] - 1) * 10000 + (this.tmpBytes[n4 + 1] - 1) * 100 + (this.tmpBytes[n4 + 2] - 1)) * factorTable[n6 + 2];
                    break;
                }
                case 4: {
                    d2 = (double)((this.tmpBytes[n4] - 1) * 1000000 + (this.tmpBytes[n4 + 1] - 1) * 10000 + (this.tmpBytes[n4 + 2] - 1) * 100 + (this.tmpBytes[n4 + 3] - 1)) * factorTable[n6 + 3];
                    break;
                }
                case 5: {
                    d2 = (double)((this.tmpBytes[n4 + 1] - 1) * 1000000 + (this.tmpBytes[n4 + 2] - 1) * 10000 + (this.tmpBytes[n4 + 3] - 1) * 100 + (this.tmpBytes[n4 + 4] - 1)) * factorTable[n6 + 4] + (double)(this.tmpBytes[n4] - 1) * factorTable[n6];
                    break;
                }
                case 6: {
                    d2 = (double)((this.tmpBytes[n4 + 2] - 1) * 1000000 + (this.tmpBytes[n4 + 3] - 1) * 10000 + (this.tmpBytes[n4 + 4] - 1) * 100 + (this.tmpBytes[n4 + 5] - 1)) * factorTable[n6 + 5] + (double)((this.tmpBytes[n4] - 1) * 100 + (this.tmpBytes[n4 + 1] - 1)) * factorTable[n6 + 1];
                    break;
                }
                default: {
                    d2 = (double)((this.tmpBytes[n4 + 3] - 1) * 1000000 + (this.tmpBytes[n4 + 4] - 1) * 10000 + (this.tmpBytes[n4 + 5] - 1) * 100 + (this.tmpBytes[n4 + 6] - 1)) * factorTable[n6 + 6] + (double)((this.tmpBytes[n4] - 1) * 10000 + (this.tmpBytes[n4 + 1] - 1) * 100 + (this.tmpBytes[n4 + 2] - 1)) * factorTable[n6 + 2];
                    break;
                }
            }
        } else {
            if (by == 0 && n3 == 1) {
                return Float.NEGATIVE_INFINITY;
            }
            byte by3 = (byte)((~by & 0xFFFFFF7F) - 65);
            int n7 = n3 - 1;
            if (n7 != 20 || this.tmpBytes[n7] == 102) {
                --n7;
            }
            while (this.tmpBytes[n4] == 1 && n7 > 0) {
                ++n4;
                --n7;
                by3 = (byte)(by3 - 1);
            }
            int n8 = (int)(127.0 - (double)by3);
            switch (n7) {
                case 1: {
                    d2 = (double)(-(101 - this.tmpBytes[n4])) * factorTable[n8];
                    break;
                }
                case 2: {
                    d2 = (double)(-((101 - this.tmpBytes[n4]) * 100 + (101 - this.tmpBytes[n4 + 1]))) * factorTable[n8 + 1];
                    break;
                }
                case 3: {
                    d2 = (double)(-((101 - this.tmpBytes[n4]) * 10000 + (101 - this.tmpBytes[n4 + 1]) * 100 + (101 - this.tmpBytes[n4 + 2]))) * factorTable[n8 + 2];
                    break;
                }
                case 4: {
                    d2 = (double)(-((101 - this.tmpBytes[n4]) * 1000000 + (101 - this.tmpBytes[n4 + 1]) * 10000 + (101 - this.tmpBytes[n4 + 2]) * 100 + (101 - this.tmpBytes[n4 + 3]))) * factorTable[n8 + 3];
                    break;
                }
                case 5: {
                    d2 = -((double)((101 - this.tmpBytes[n4 + 1]) * 1000000 + (101 - this.tmpBytes[n4 + 2]) * 10000 + (101 - this.tmpBytes[n4 + 3]) * 100 + (101 - this.tmpBytes[n4 + 4])) * factorTable[n8 + 4] + (double)(101 - this.tmpBytes[n4]) * factorTable[n8]);
                    break;
                }
                case 6: {
                    d2 = -((double)((101 - this.tmpBytes[n4 + 2]) * 1000000 + (101 - this.tmpBytes[n4 + 3]) * 10000 + (101 - this.tmpBytes[n4 + 4]) * 100 + (101 - this.tmpBytes[n4 + 5])) * factorTable[n8 + 5] + (double)((101 - this.tmpBytes[n4]) * 100 + (101 - this.tmpBytes[n4 + 1])) * factorTable[n8 + 1]);
                    break;
                }
                default: {
                    d2 = -((double)((101 - this.tmpBytes[n4 + 3]) * 1000000 + (101 - this.tmpBytes[n4 + 4]) * 10000 + (101 - this.tmpBytes[n4 + 5]) * 100 + (101 - this.tmpBytes[n4 + 6])) * factorTable[n8 + 6] + (double)((101 - this.tmpBytes[n4]) * 10000 + (101 - this.tmpBytes[n4 + 1]) * 100 + (101 - this.tmpBytes[n4 + 2])) * factorTable[n8 + 2]);
                }
            }
        }
        return (float)d2;
    }

    @Override
    double getDouble(int n2) throws SQLException {
        double d2;
        int n3;
        boolean bl;
        byte by;
        if (this.isUseLess || this.isNull(n2)) {
            return 0.0;
        }
        int n4 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n4);
        byte by2 = this.tmpBytes[0];
        int n5 = 1;
        int n6 = n4 - 1;
        boolean bl2 = true;
        if ((by2 & 0xFFFFFF80) != 0) {
            if (by2 == -128 && n4 == 1) {
                return 0.0;
            }
            if (n4 == 2 && by2 == -1 && this.tmpBytes[1] == 101) {
                return Double.POSITIVE_INFINITY;
            }
            by = (byte)((by2 & 0xFFFFFF7F) - 65);
            bl = (this.tmpBytes[n5 + n6 - 1] - 1) % 10 == 0;
            n3 = this.tmpBytes[n5] - 1;
        } else {
            bl2 = false;
            if (by2 == 0 && n4 == 1) {
                return Double.NEGATIVE_INFINITY;
            }
            by = (byte)((~by2 & 0xFFFFFF7F) - 65);
            if (n6 != 20 || this.tmpBytes[n6] == 102) {
                --n6;
            }
            bl = (101 - this.tmpBytes[n5 + n6 - 1]) % 10 == 0;
            n3 = 101 - this.tmpBytes[n5];
        }
        int n7 = n6 << 1;
        if (bl) {
            --n7;
        }
        int n8 = (by + 1 << 1) - n7;
        if (n3 < 10) {
            --n7;
        }
        if (n7 <= 15 && (n8 >= 0 && n8 <= 37 - n7 || n8 < 0 && n8 >= -22)) {
            double d3;
            int n9 = 0;
            int n10 = 0;
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            int n15 = 0;
            if (bl2) {
                switch (n6) {
                    default: {
                        n15 = this.tmpBytes[n5 + 7] - 1;
                    }
                    case 7: {
                        n14 = this.tmpBytes[n5 + 6] - 1;
                    }
                    case 6: {
                        n13 = this.tmpBytes[n5 + 5] - 1;
                    }
                    case 5: {
                        n12 = this.tmpBytes[n5 + 4] - 1;
                    }
                    case 4: {
                        n11 = this.tmpBytes[n5 + 3] - 1;
                    }
                    case 3: {
                        n10 = this.tmpBytes[n5 + 2] - 1;
                    }
                    case 2: {
                        n9 = this.tmpBytes[n5 + 1] - 1;
                    }
                    case 1: 
                }
            } else {
                switch (n6) {
                    default: {
                        n15 = 101 - this.tmpBytes[n5 + 7];
                    }
                    case 7: {
                        n14 = 101 - this.tmpBytes[n5 + 6];
                    }
                    case 6: {
                        n13 = 101 - this.tmpBytes[n5 + 5];
                    }
                    case 5: {
                        n12 = 101 - this.tmpBytes[n5 + 4];
                    }
                    case 4: {
                        n11 = 101 - this.tmpBytes[n5 + 3];
                    }
                    case 3: {
                        n10 = 101 - this.tmpBytes[n5 + 2];
                    }
                    case 2: {
                        n9 = 101 - this.tmpBytes[n5 + 1];
                    }
                    case 1: 
                }
            }
            if (bl) {
                switch (n6) {
                    default: {
                        d3 = n3 / 10;
                        break;
                    }
                    case 2: {
                        d3 = n3 * 10 + n9 / 10;
                        break;
                    }
                    case 3: {
                        d3 = n3 * 1000 + n9 * 10 + n10 / 10;
                        break;
                    }
                    case 4: {
                        d3 = n3 * 100000 + n9 * 1000 + n10 * 10 + n11 / 10;
                        break;
                    }
                    case 5: {
                        d3 = n3 * 10000000 + n9 * 100000 + n10 * 1000 + n11 * 10 + n12 / 10;
                        break;
                    }
                    case 6: {
                        int n16 = n9 * 10000000 + n10 * 100000 + n11 * 1000 + n12 * 10 + n13 / 10;
                        d3 = (long)n3 * 1000000000L + (long)n16;
                        break;
                    }
                    case 7: {
                        int n17 = n10 * 10000000 + n11 * 100000 + n12 * 1000 + n13 * 10 + n14 / 10;
                        int n18 = n3 * 100 + n9;
                        d3 = (long)n18 * 1000000000L + (long)n17;
                        break;
                    }
                    case 8: {
                        int n19 = n11 * 10000000 + n12 * 100000 + n13 * 1000 + n14 * 10 + n15 / 10;
                        int n20 = n3 * 10000 + n9 * 100 + n10;
                        d3 = (long)n20 * 1000000000L + (long)n19;
                        break;
                    }
                }
            } else {
                switch (n6) {
                    default: {
                        d3 = n3;
                        break;
                    }
                    case 2: {
                        d3 = n3 * 100 + n9;
                        break;
                    }
                    case 3: {
                        d3 = n3 * 10000 + n9 * 100 + n10;
                        break;
                    }
                    case 4: {
                        d3 = n3 * 1000000 + n9 * 10000 + n10 * 100 + n11;
                        break;
                    }
                    case 5: {
                        int n21 = n9 * 1000000 + n10 * 10000 + n11 * 100 + n12;
                        d3 = (long)n3 * 100000000L + (long)n21;
                        break;
                    }
                    case 6: {
                        int n22 = n10 * 1000000 + n11 * 10000 + n12 * 100 + n13;
                        int n23 = n3 * 100 + n9;
                        d3 = (long)n23 * 100000000L + (long)n22;
                        break;
                    }
                    case 7: {
                        int n24 = n11 * 1000000 + n12 * 10000 + n13 * 100 + n14;
                        int n25 = n3 * 10000 + n9 * 100 + n10;
                        d3 = (long)n25 * 100000000L + (long)n24;
                        break;
                    }
                    case 8: {
                        int n26 = n12 * 1000000 + n13 * 10000 + n14 * 100 + n15;
                        int n27 = n3 * 1000000 + n9 * 10000 + n10 * 100 + n11;
                        d3 = (long)n27 * 100000000L + (long)n26;
                    }
                }
            }
            if (n8 == 0 || d3 == 0.0) {
                d2 = d3;
            } else if (n8 >= 0) {
                if (n8 <= 22) {
                    d2 = d3 * small10pow[n8];
                } else {
                    int n28 = 15 - n7;
                    d2 = (d3 *= small10pow[n28]) * small10pow[n8 - n28];
                }
            } else {
                d2 = d3 / small10pow[-n8];
            }
        } else {
            int n29;
            int n30;
            int n31;
            int n32;
            int n33;
            int n34;
            int n35;
            int n36;
            int n37 = 0;
            int n38 = 0;
            int n39 = 0;
            int n40 = 0;
            int n41 = 0;
            int n42 = 0;
            int n43 = 0;
            int n44 = 0;
            int n45 = 0;
            int n46 = 0;
            int n47 = 0;
            int n48 = 0;
            int n49 = 0;
            int n50 = 0;
            int n51 = 0;
            boolean bl3 = false;
            int n52 = 0;
            if (bl2) {
                if ((n6 & 1) != 0) {
                    n36 = 2;
                    n37 = n3;
                } else {
                    n36 = 3;
                    n37 = n3 * 100 + (this.tmpBytes[n5 + 1] - 1);
                }
                while (n36 < n6) {
                    n35 = (this.tmpBytes[n5 + n36 - 1] - 1) * 100 + (this.tmpBytes[n5 + n36] - 1) + n37 * 10000;
                    switch (n46) {
                        default: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n43 * 10000;
                            n43 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n44 * 10000;
                            n44 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n45 * 10000;
                            n45 = n35 & 0xFFFF;
                            break;
                        }
                        case 7: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n43 * 10000;
                            n43 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n44 * 10000;
                            n44 = n35 & 0xFFFF;
                            break;
                        }
                        case 6: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n43 * 10000;
                            n43 = n35 & 0xFFFF;
                            break;
                        }
                        case 5: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            break;
                        }
                        case 4: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            break;
                        }
                        case 3: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            break;
                        }
                        case 2: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            break;
                        }
                        case 1: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            break;
                        }
                        case 0: {
                            n37 = n35 & 0xFFFF;
                        }
                    }
                    n35 = n35 >> 16 & 0xFFFF;
                    if (n35 != 0) {
                        switch (++n46) {
                            case 8: {
                                n45 = n35;
                                break;
                            }
                            case 7: {
                                n44 = n35;
                                break;
                            }
                            case 6: {
                                n43 = n35;
                                break;
                            }
                            case 5: {
                                n42 = n35;
                                break;
                            }
                            case 4: {
                                n41 = n35;
                                break;
                            }
                            case 3: {
                                n40 = n35;
                                break;
                            }
                            case 2: {
                                n39 = n35;
                                break;
                            }
                            case 1: {
                                n38 = n35;
                                break;
                            }
                        }
                    }
                    n36 += 2;
                }
            } else {
                if ((n6 & 1) != 0) {
                    n36 = 2;
                    n37 = n3;
                } else {
                    n36 = 3;
                    n37 = n3 * 100 + (101 - this.tmpBytes[n5 + 1]);
                }
                while (n36 < n6) {
                    n35 = (101 - this.tmpBytes[n5 + n36 - 1]) * 100 + (101 - this.tmpBytes[n5 + n36]) + n37 * 10000;
                    switch (n46) {
                        default: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n43 * 10000;
                            n43 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n44 * 10000;
                            n44 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n45 * 10000;
                            n45 = n35 & 0xFFFF;
                            break;
                        }
                        case 7: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n43 * 10000;
                            n43 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n44 * 10000;
                            n44 = n35 & 0xFFFF;
                            break;
                        }
                        case 6: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n43 * 10000;
                            n43 = n35 & 0xFFFF;
                            break;
                        }
                        case 5: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n42 * 10000;
                            n42 = n35 & 0xFFFF;
                            break;
                        }
                        case 4: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n41 * 10000;
                            n41 = n35 & 0xFFFF;
                            break;
                        }
                        case 3: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n40 * 10000;
                            n40 = n35 & 0xFFFF;
                            break;
                        }
                        case 2: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n39 * 10000;
                            n39 = n35 & 0xFFFF;
                            break;
                        }
                        case 1: {
                            n37 = n35 & 0xFFFF;
                            n35 = (n35 >> 16 & 0xFFFF) + n38 * 10000;
                            n38 = n35 & 0xFFFF;
                            break;
                        }
                        case 0: {
                            n37 = n35 & 0xFFFF;
                        }
                    }
                    n35 = n35 >> 16 & 0xFFFF;
                    if (n35 != 0) {
                        switch (++n46) {
                            case 8: {
                                n45 = n35;
                                break;
                            }
                            case 7: {
                                n44 = n35;
                                break;
                            }
                            case 6: {
                                n43 = n35;
                                break;
                            }
                            case 5: {
                                n42 = n35;
                                break;
                            }
                            case 4: {
                                n41 = n35;
                                break;
                            }
                            case 3: {
                                n40 = n35;
                                break;
                            }
                            case 2: {
                                n39 = n35;
                                break;
                            }
                            case 1: {
                                n38 = n35;
                                break;
                            }
                        }
                    }
                    n36 += 2;
                }
            }
            int n53 = n46++;
            int n54 = 62 - by + n6;
            int n55 = nexpdigstable[n54];
            int[] nArray = expdigstable[n54];
            n52 = n46 + 5;
            n35 = 0;
            if (n55 > n52) {
                n35 = n55 - n52;
                n55 = n52;
            }
            int n56 = 0;
            int n57 = 0;
            int n58 = n55 - 1 + (n46 - 1) - 4;
            for (n34 = 0; n34 < n58; ++n34) {
                n33 = n57 & 0xFFFF;
                n57 = n57 >> 16 & 0xFFFF;
                n32 = n46 < n34 + 1 ? n46 : n34 + 1;
                int n59 = n31 = n34 - n55 + 1 > 0 ? n34 - n55 + 1 : 0;
                while (n31 < n32) {
                    n30 = n35 + n34 - n31;
                    switch (n31) {
                        case 8: {
                            n29 = n45 * nArray[n30];
                            break;
                        }
                        case 7: {
                            n29 = n44 * nArray[n30];
                            break;
                        }
                        case 6: {
                            n29 = n43 * nArray[n30];
                            break;
                        }
                        case 5: {
                            n29 = n42 * nArray[n30];
                            break;
                        }
                        case 4: {
                            n29 = n41 * nArray[n30];
                            break;
                        }
                        case 3: {
                            n29 = n40 * nArray[n30];
                            break;
                        }
                        case 2: {
                            n29 = n39 * nArray[n30];
                            break;
                        }
                        case 1: {
                            n29 = n38 * nArray[n30];
                            break;
                        }
                        default: {
                            n29 = n37 * nArray[n30];
                        }
                    }
                    n33 += n29 & 0xFFFF;
                    n57 += n29 >> 16 & 0xFFFF;
                    ++n31;
                }
                bl3 = bl3 || (n33 & 0xFFFF) != 0;
                n57 += n33 >> 16 & 0xFFFF;
            }
            n58 += 5;
            while (n34 < n58) {
                n33 = n57 & 0xFFFF;
                n57 = n57 >> 16 & 0xFFFF;
                n32 = n46 < n34 + 1 ? n46 : n34 + 1;
                int n60 = n31 = n34 - n55 + 1 > 0 ? n34 - n55 + 1 : 0;
                while (n31 < n32) {
                    n30 = n35 + n34 - n31;
                    switch (n31) {
                        case 8: {
                            n29 = n45 * nArray[n30];
                            break;
                        }
                        case 7: {
                            n29 = n44 * nArray[n30];
                            break;
                        }
                        case 6: {
                            n29 = n43 * nArray[n30];
                            break;
                        }
                        case 5: {
                            n29 = n42 * nArray[n30];
                            break;
                        }
                        case 4: {
                            n29 = n41 * nArray[n30];
                            break;
                        }
                        case 3: {
                            n29 = n40 * nArray[n30];
                            break;
                        }
                        case 2: {
                            n29 = n39 * nArray[n30];
                            break;
                        }
                        case 1: {
                            n29 = n38 * nArray[n30];
                            break;
                        }
                        default: {
                            n29 = n37 * nArray[n30];
                        }
                    }
                    n33 += n29 & 0xFFFF;
                    n57 += n29 >> 16 & 0xFFFF;
                    ++n31;
                }
                switch (n56++) {
                    case 4: {
                        n51 = n33 & 0xFFFF;
                        break;
                    }
                    case 3: {
                        n50 = n33 & 0xFFFF;
                        break;
                    }
                    case 2: {
                        n49 = n33 & 0xFFFF;
                        break;
                    }
                    case 1: {
                        n48 = n33 & 0xFFFF;
                        break;
                    }
                    default: {
                        n47 = n33 & 0xFFFF;
                    }
                }
                n57 += n33 >> 16 & 0xFFFF;
                ++n34;
            }
            while (n57 != 0) {
                if (n56 < 5) {
                    switch (n56++) {
                        case 4: {
                            n51 = n57 & 0xFFFF;
                            break;
                        }
                        case 3: {
                            n50 = n57 & 0xFFFF;
                            break;
                        }
                        case 2: {
                            n49 = n57 & 0xFFFF;
                            break;
                        }
                        case 1: {
                            n48 = n57 & 0xFFFF;
                            break;
                        }
                        default: {
                            n47 = n57 & 0xFFFF;
                            break;
                        }
                    }
                } else {
                    bl3 = bl3 || n47 != 0;
                    n47 = n48;
                    n48 = n49;
                    n49 = n50;
                    n50 = n51;
                    n51 = n57 & 0xFFFF;
                }
                n57 = n57 >> 16 & 0xFFFF;
                ++n53;
            }
            int n61 = (binexpstable[n54] + n53) * 16 - 1;
            switch (n56) {
                case 5: {
                    n33 = n51;
                    break;
                }
                case 4: {
                    n33 = n50;
                    break;
                }
                case 3: {
                    n33 = n49;
                    break;
                }
                case 2: {
                    n33 = n48;
                    break;
                }
                default: {
                    n33 = n47;
                }
            }
            for (n32 = n33 >> 1; n32 != 0; n32 >>= 1) {
                ++n61;
            }
            n32 = 5;
            n30 = n33 << 5;
            n29 = 0;
            n57 = 0;
            while ((n30 & 0x100000) == 0) {
                n30 <<= 1;
                ++n32;
            }
            switch (n56) {
                case 5: {
                    if (n32 > 16) {
                        n30 |= n50 << n32 - 16 | n49 >> 32 - n32;
                        n29 = n49 << n32 | n48 << n32 - 16 | n47 >> 32 - n32;
                        n57 = n47 & 1 << 31 - n32;
                        bl3 = bl3 || n47 << n32 + 1 != 0;
                        break;
                    }
                    if (n32 == 16) {
                        n30 |= n50;
                        n29 = n49 << 16 | n48;
                        n57 = n47 & 0x8000;
                        bl3 = bl3 || (n47 & Short.MAX_VALUE) != 0;
                        break;
                    }
                    n30 |= n50 >> 16 - n32;
                    n29 = n50 << 16 + n32 | n49 << n32 | n48 >> 16 - n32;
                    n57 = n48 & 1 << 15 - n32;
                    if (n32 < 15) {
                        bl3 = bl3 || n48 << n32 + 17 != 0;
                    }
                    bl3 = bl3 || n47 != 0;
                    break;
                }
                case 4: {
                    if (n32 > 16) {
                        n30 |= n49 << n32 - 16 | n48 >> 32 - n32;
                        n29 = n48 << n32 | n47 << n32 - 16;
                        break;
                    }
                    if (n32 == 16) {
                        n30 |= n49;
                        n29 = n48 << 16 | n47;
                        break;
                    }
                    n30 |= n49 >> 16 - n32;
                    n29 = n49 << 16 + n32 | n48 << n32 | n47 >> 16 - n32;
                    n57 = n47 & 1 << 15 - n32;
                    if (n32 >= 15) break;
                    bl3 = bl3 || n47 << n32 + 17 != 0;
                    break;
                }
                case 3: {
                    if (n32 > 16) {
                        n30 |= n48 << n32 - 16 | n47 >> 32 - n32;
                        n29 = n47 << n32;
                        break;
                    }
                    if (n32 == 16) {
                        n30 |= n48;
                        n29 = n47 << 16;
                        break;
                    }
                    n30 |= n48 >> 16 - n32;
                    n29 = n48 << 16 + n32;
                    n29 |= n47 << n32;
                    break;
                }
                case 2: {
                    if (n32 > 16) {
                        n30 |= n47 << n32 - 16;
                        break;
                    }
                    if (n32 == 16) {
                        n30 |= n47;
                        break;
                    }
                    n30 |= n47 >> 16 - n32;
                    n29 = n47 << 16 + n32;
                    break;
                }
            }
            if (n57 != 0 && (bl3 || (n29 & 1) != 0)) {
                if (n29 == -1) {
                    n29 = 0;
                    if ((++n30 & 0x200000) != 0) {
                        n29 = n29 >> 1 | n30 << 31;
                        n30 >>= 1;
                        ++n61;
                    }
                } else {
                    ++n29;
                }
            }
            long l2 = (long)n61 << 52 | (long)(n30 & 0xFFFFF) << 32 | (long)n29 & 0xFFFFFFFFL;
            d2 = Double.longBitsToDouble(l2);
        }
        return bl2 ? d2 : -d2;
    }

    double getDoubleImprecise(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return 0.0;
        }
        int n3 = this.getLength(n2);
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n3);
        byte by = this.tmpBytes[0];
        double d2 = 0.0;
        int n4 = 1;
        if ((by & 0xFFFFFF80) != 0) {
            if (by == -128 && n3 == 1) {
                return 0.0;
            }
            if (n3 == 2 && by == -1 && this.tmpBytes[1] == 101) {
                return Double.POSITIVE_INFINITY;
            }
            byte by2 = (byte)((by & 0xFFFFFF7F) - 65);
            int n5 = n3 - 1;
            int n6 = (int)(127.0 - (double)by2);
            int n7 = n5 % 4;
            switch (n7) {
                case 1: {
                    d2 = (double)(this.tmpBytes[n4] - 1) * factorTable[n6];
                    break;
                }
                case 2: {
                    d2 = (double)((this.tmpBytes[n4] - 1) * 100 + (this.tmpBytes[n4 + 1] - 1)) * factorTable[n6 + 1];
                    break;
                }
                case 3: {
                    d2 = (double)((this.tmpBytes[n4] - 1) * 10000 + (this.tmpBytes[n4 + 1] - 1) * 100 + (this.tmpBytes[n4 + 2] - 1)) * factorTable[n6 + 2];
                    break;
                }
            }
            while (n7 < n5) {
                d2 += (double)((this.tmpBytes[n4 + n7] - 1) * 1000000 + (this.tmpBytes[n4 + n7 + 1] - 1) * 10000 + (this.tmpBytes[n4 + n7 + 2] - 1) * 100 + (this.tmpBytes[n4 + n7 + 3] - 1)) * factorTable[n6 + n7 + 3];
                n7 += 4;
            }
        } else {
            if (by == 0 && n3 == 1) {
                return Double.NEGATIVE_INFINITY;
            }
            byte by3 = (byte)((~by & 0xFFFFFF7F) - 65);
            int n8 = n3 - 1;
            if (n8 != 20 || this.tmpBytes[n8] == 102) {
                --n8;
            }
            int n9 = (int)(127.0 - (double)by3);
            int n10 = n8 % 4;
            switch (n10) {
                case 1: {
                    d2 = (double)(101 - this.tmpBytes[n4]) * factorTable[n9];
                    break;
                }
                case 2: {
                    d2 = (double)((101 - this.tmpBytes[n4]) * 100 + (101 - this.tmpBytes[n4 + 1])) * factorTable[n9 + 1];
                    break;
                }
                case 3: {
                    d2 = (double)((101 - this.tmpBytes[n4]) * 10000 + (101 - this.tmpBytes[n4 + 1]) * 100 + (101 - this.tmpBytes[n4 + 2])) * factorTable[n9 + 2];
                    break;
                }
            }
            while (n10 < n8) {
                d2 += (double)((101 - this.tmpBytes[n4 + n10]) * 1000000 + (101 - this.tmpBytes[n4 + n10 + 1]) * 10000 + (101 - this.tmpBytes[n4 + n10 + 2]) * 100 + (101 - this.tmpBytes[n4 + n10 + 3])) * factorTable[n9 + n10 + 3];
                n10 += 4;
            }
            d2 = -d2;
        }
        return d2;
    }

    @Override
    BigDecimal getBigDecimal(int n2) throws SQLException {
        int n3;
        int n4;
        byte[] byArray;
        int n5;
        int n6;
        int n7;
        int n8;
        byte by;
        int n9;
        int n10;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        int n11 = this.getLength(n2);
        assert (n11 > 0) : "Length not positive";
        this.rowData.get(this.getOffset(n2), this.tmpBytes, 0, n11);
        for (n10 = 0; n10 < 27; ++n10) {
            this.digs[n10] = 0;
        }
        n10 = 0;
        int n12 = 1;
        int n13 = 26;
        int n14 = 0;
        byte by2 = this.tmpBytes[0];
        boolean bl = false;
        if ((by2 & 0xFFFFFF80) != 0) {
            if (by2 == -128 && n11 == 1) {
                return BIGDEC_ZERO;
            }
            if (n11 == 2 && by2 == -1 && this.tmpBytes[1] == 101) {
                this.throwOverflow();
            }
            n9 = 1;
            by = (byte)((by2 & 0xFFFFFF7F) - 65);
            n8 = n11 - 1;
            n7 = n8 - 1;
            n6 = by - n8 + 1 << 1;
            if (n6 > 0) {
                n6 = 0;
                n7 = by;
            } else if (n6 < 0) {
                bl = (this.tmpBytes[n8] - 1) % 10 == 0;
            }
            int n15 = n12;
            n12 = (byte)(n12 + 1);
            n10 = this.tmpBytes[n15] - 1;
            while ((n7 & 1) != 0) {
                if (n12 > n8) {
                    n10 *= 100;
                } else {
                    int n16 = n12;
                    n12 = (byte)(n12 + 1);
                    n10 = n10 * 100 + (this.tmpBytes[n16] - 1);
                }
                --n7;
            }
        } else {
            if (by2 == 0 && n11 == 1) {
                this.throwOverflow();
            }
            n9 = -1;
            by = (byte)((~by2 & 0xFFFFFF7F) - 65);
            n8 = n11 - 1;
            if (n8 != 20 || this.tmpBytes[n8] == 102) {
                --n8;
            }
            n7 = n8 - 1;
            n6 = by - n8 + 1 << 1;
            if (n6 > 0) {
                n6 = 0;
                n7 = by;
            } else if (n6 < 0) {
                bl = (101 - this.tmpBytes[n8]) % 10 == 0;
            }
            int n17 = n12;
            n12 = (byte)(n12 + 1);
            n10 = 101 - this.tmpBytes[n17];
            while ((n7 & 1) != 0) {
                if (n12 > n8) {
                    n10 *= 100;
                } else {
                    int n18 = n12;
                    n12 = (byte)(n12 + 1);
                    n10 = n10 * 100 + (101 - this.tmpBytes[n18]);
                }
                --n7;
            }
        }
        if (bl) {
            ++n6;
            n10 /= 10;
        }
        int n19 = n8 - 1;
        while (n7 != 0) {
            int n20;
            if (n9 == 1) {
                if (bl) {
                    n14 = (this.tmpBytes[n12 - 1] - 1) % 10 * 1000 + (this.tmpBytes[n12] - 1) * 10 + (this.tmpBytes[n12 + 1] - 1) / 10 + n10 * 10000;
                    n12 = (byte)(n12 + 2);
                } else if (n12 < n19) {
                    n14 = (this.tmpBytes[n12] - 1) * 100 + (this.tmpBytes[n12 + 1] - 1) + n10 * 10000;
                    n12 = (byte)(n12 + 2);
                } else {
                    n14 = 0;
                    if (n12 <= n8) {
                        n20 = 0;
                        while (n12 <= n8) {
                            int n21 = n12;
                            n12 = (byte)(n12 + 1);
                            n14 = n14 * 100 + (this.tmpBytes[n21] - 1);
                            ++n20;
                        }
                        while (n20 < 2) {
                            n14 *= 100;
                            ++n20;
                        }
                    }
                    n14 += n10 * 10000;
                }
            } else if (bl) {
                n14 = (101 - this.tmpBytes[n12 - 1]) % 10 * 1000 + (101 - this.tmpBytes[n12]) * 10 + (101 - this.tmpBytes[n12 + 1]) / 10 + n10 * 10000;
                n12 = (byte)(n12 + 2);
            } else if (n12 < n19) {
                n14 = (101 - this.tmpBytes[n12]) * 100 + (101 - this.tmpBytes[n12 + 1]) + n10 * 10000;
                n12 = (byte)(n12 + 2);
            } else {
                n14 = 0;
                if (n12 <= n8) {
                    n20 = 0;
                    while (n12 <= n8) {
                        int n22 = n12;
                        n12 = (byte)(n12 + 1);
                        n14 = n14 * 100 + (101 - this.tmpBytes[n22]);
                        ++n20;
                    }
                    while (n20 < 2) {
                        n14 *= 100;
                        ++n20;
                    }
                }
                n14 += n10 * 10000;
            }
            n10 = n14 & 0xFFFF;
            for (n20 = 25; n20 >= n13; --n20) {
                n14 = (n14 >> 16) + this.digs[n20] * 10000;
                this.digs[n20] = n14 & 0xFFFF;
            }
            if (n13 > 0 && (n14 >>= 16) != 0) {
                n13 = (byte)(n13 - 1);
                this.digs[n13] = n14;
            }
            n7 -= 2;
        }
        this.digs[26] = n10;
        byte by3 = (byte)(this.digs[n13] >> 8 & 0xFF);
        if (by3 == 0) {
            n5 = 53 - (n13 << 1);
            byArray = new byte[n5];
            for (n4 = 26; n4 > n13; --n4) {
                n3 = n4 - n13 << 1;
                byArray[n3 - 1] = (byte)(this.digs[n4] >> 8 & 0xFF);
                byArray[n3] = (byte)(this.digs[n4] & 0xFF);
            }
            byArray[0] = (byte)(this.digs[n13] & 0xFF);
        } else {
            n5 = 54 - (n13 << 1);
            byArray = new byte[n5];
            for (n4 = 26; n4 > n13; --n4) {
                n3 = n4 - n13 << 1;
                byArray[n3] = (byte)(this.digs[n4] >> 8 & 0xFF);
                byArray[n3 + 1] = (byte)(this.digs[n4] & 0xFF);
            }
            byArray[0] = by3;
            byArray[1] = (byte)(this.digs[n13] & 0xFF);
        }
        if (n6 == 0 && n5 < 8 && n5 > 0) {
            long l2 = (long)byArray[0] & 0xFFL;
            for (int i2 = 1; i2 < n5; ++i2) {
                l2 = l2 << 8 | (long)(byArray[i2] & 0xFF);
            }
            return new BigDecimal(l2 *= (long)n9);
        }
        BigInteger bigInteger = new BigInteger(n9, byArray);
        return new BigDecimal(bigInteger, -n6);
    }

    @Override
    BigDecimal getBigDecimal(int n2, int n3) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return this.getBigDecimal(n2).setScale(n3, 6);
    }

    @Override
    BigInteger getBigInteger(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return this.getBigDecimal(n2).toBigInteger();
    }

    @Override
    String getString(int n2) throws SQLException {
        String string;
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        byte[] byArray = this.getBytesInternal(n2);
        int n3 = byArray.length;
        NUMBER nUMBER = new NUMBER(byArray);
        String string2 = oracle.sql.NUMBER.toString(byArray);
        int n4 = string2.length();
        if (string2.startsWith("0.") || string2.startsWith("-0.")) {
            --n4;
        }
        if (n4 > 38) {
            string2 = nUMBER.toText(-44, null);
            int n5 = string2.indexOf(69);
            int n6 = string2.indexOf(43);
            if (n5 == -1) {
                n5 = string2.indexOf(101);
            }
            int n7 = n5 - 1;
            while (string2.charAt(n7) == '0') {
                --n7;
            }
            String string3 = string2.substring(0, n7 + 1);
            String string4 = null;
            string4 = n6 > 0 ? string2.substring(n6 + 1) : string2.substring(n5 + 1);
            string = (string3 + "E" + string4).trim();
        } else {
            string = nUMBER.toText(38, null).trim();
        }
        if (string.startsWith("-.")) {
            return "-0" + string.substring(1);
        }
        if (string.startsWith(".")) {
            return "0" + string;
        }
        return string;
    }

    @Override
    NUMBER getNUMBER(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return new NUMBER(this.getBytesInternal(n2));
    }

    @Override
    Object getObject(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        Number number = null;
        if (this.externalType == 0) {
            number = this.statement.connection.j2ee13Compliant && this.precision != 0 && this.scale == -127 ? new Double(this.getDouble(n2)) : this.getBigDecimal(n2);
        } else {
            switch (this.externalType) {
                case -7: {
                    return this.getBoolean(n2);
                }
                case -6: {
                    return this.getByte(n2);
                }
                case 5: {
                    return this.getShort(n2);
                }
                case 4: {
                    return this.getInt(n2);
                }
                case -5: {
                    return this.getLong(n2);
                }
                case 6: 
                case 8: {
                    return this.getDouble(n2);
                }
                case 7: {
                    return Float.valueOf(this.getFloat(n2));
                }
                case 2: 
                case 3: {
                    return this.getBigDecimal(n2);
                }
            }
            throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 4).fillInStackTrace();
        }
        return number;
    }

    @Override
    Object getObject(int n2, Map<String, Class<?>> map) throws SQLException {
        return this.getObject(n2);
    }

    @Override
    Datum getOracleObject(int n2) throws SQLException {
        return this.getNUMBER(n2);
    }

    @Override
    byte[] getBytes(int n2) throws SQLException {
        if (this.isUseLess || this.isNull(n2)) {
            return null;
        }
        return this.getBytesInternal(n2);
    }

    void throwOverflow() throws SQLException {
        throw (SQLException)DatabaseError.createSqlException(this.getConnectionDuringExceptionHandling(), 26).fillInStackTrace();
    }
}

